(ns snake.app-component
  (:require [reagent.core :as reagent]
            [snake.core :as core]))

(defn get-button-text
  [state]
  (let [game-state (:game-state state)]
    (cond (= game-state "idle") "Start game"
          (= game-state "active") "Stop game")))

(defn draw-background
  [ctx width height]
  (set! (.-fillStyle ctx) "green")
  (.fillRect ctx 0 0 width height))

(defn draw-snake
  [state ctx]
  (set! (.-fillStyle ctx) "black")
  (doseq [coordinate (core/get-snake-coordinates state)]
    (.fillRect ctx
               (* (first coordinate) 10)
               (* (last coordinate) 10)
               10
               10)))

(defn draw-food
  [state ctx]
  (let [food-coordinates (core/get-food-coordinates state)]
    (set! (.-fillStyle ctx) "purple")
    (.fillRect ctx
               (* (first food-coordinates) 10)
               (* (last food-coordinates) 10)
               10
               10))
  )

(defn draw-canvas-contents [canvas state]
  (let [ctx (.getContext canvas "2d")
        width (get-in state [:game-size :width])
        height (get-in state [:game-size :height])]
    (draw-background ctx width height)
    (draw-snake state ctx)
    (draw-food state ctx)))

(defn game-component
  [{state :state trigger-event :trigger-event}]
  (let [local-state (reagent/atom nil)]
    (reagent/create-class
      {:component-did-update
       (fn [this]
         (draw-canvas-contents @local-state (:state (reagent.core/props this))))

       :component-did-mount
       (fn [this]
         (draw-canvas-contents @local-state (:state (reagent.core/props this))))

       :reagent-render
       (fn []
         [:div
          [:canvas {:ref    (fn [canvas] (reset! local-state canvas))
                    :width  (get-in state [:game-size :width])
                    :height (get-in state [:game-size :height])
                    :style  {:border "1px solid black"}}]
          [:div [:button {:on-click (fn [] (trigger-event {:name :start-stop-game-clicked}))}
                 (get-button-text state)]]])})))

(defn app-component
  [{app-state-atom :app-state-atom
    trigger-event  :trigger-event}]
  [game-component {:state @app-state-atom :trigger-event trigger-event}])

