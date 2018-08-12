(ns snake.app-component
  (:require [reagent.core :as reagent]))


(defn draw-canvas [state]
  (println "drawing canvas")
  (let [canvas (:canvas state)]
    (println canvas)
    ))

(defn snake-component [app-state-atom]
  (let [state @app-state-atom]
   (reagent/create-class
     {:component-did-mount
                    (fn [] (println "I mounted") (draw-canvas state))

      ;; ... other methods go here
      :component-did-update
                    (fn [] (println "state is :") (draw-canvas state))

      ;; name your component for inclusion in error messages
      :display-name "snake-component"

      ;; note the keyword for this method
      :reagent-render
                    (fn []
                      [:canvas {
                                :ref    #(swap! state assoc :canvas %)
                                :id     "snake"
                                :width  (.-innerWidth js/window)
                                :height (.-innerHeight js/window)
                                :style  {:background "green" :border "1px solid black"}}

                       ]
                      )

      })))

(defn app-component [app-state-atom]
  [snake-component app-state-atom]
  )