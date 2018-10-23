(ns snake.app-component
  (:require [reagent.core :as reagent]))


(defn get-button-text
  [state]
  (let [game-state (:game-state state)]
    (cond (= game-state "idle") "Start game"
          (= game-state "active") "Stop game")))

(defn draw-canvas-contents [canvas]
  (let [ctx (.getContext canvas "2d")
        w (.-clientWidth canvas)
        h (.-clientHeight canvas)]
    (.fillRect ctx 10 10 w h)))


(defn game-component
  [_]
  (let [local-state (reagent/atom nil)]
    (reagent/create-class
      {:component-did-update
       (fn [this]
         (println "i updated")
         (draw-canvas-contents (.-firstChild @local-state)))

       :component-did-mount
       (fn [this]
         (println "i mounted")
         (reset! local-state (reagent/dom-node this)))

       :reagent-render
       (fn [{state :state trigger-event :trigger-event}]
         (println "i rendered")
         [:div
          ;; reagent-render is called before the compoment mounts, so
          ;; protect against the null dom-node that occurs on the first
          ;; render
          [:canvas (if-let [node @local-state]
                     {:width  (.-clientWidth node)
                      :height (.-clientHeight node)})]
          [:div [:button {:on-click (fn [] (trigger-event {:name :start-game-clicked}))} (get-button-text state)]]])})))

  (defn app-component
    [{app-state-atom :app-state-atom
      trigger-event  :trigger-event}]
    (let [state @app-state-atom]
      [game-component {:state state :trigger-event trigger-event}]))

