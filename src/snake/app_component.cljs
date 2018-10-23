(ns snake.app-component)


(defn draw-board-coordinates
  [coordinates]
  [:rect
   {
    :key coordinates
    :x      (first coordinates)
    :y      (last coordinates)
    :width  1
    :height 1
    :fill   "green"
    }
   ]
  )


(defn draw-snake-coordinates
  [coordinates]
  [:rect
   {
    :key coordinates
    :x      (first coordinates)
    :y      (last coordinates)
    :width  1
    :height 1
    :fill   "black"
    }
   ]
  )



(defn snake-component [app-state-atom]
  (let [state @app-state-atom]
    [:center
     [:h1 (:game-header state)]
     [:div {:style {:width 500 :height 333 :border "1px solid black" :backgroundColor "green"}}
      [:svg
       {:view-box "0 0 15 10"
        :width    500
        :height   333}
       (map (fn [coordinates]
              (draw-snake-coordinates coordinates)
              ) (:snake state))
       ]
      ]
     ]

    ))

(defn app-component [app-state-atom]
  [snake-component app-state-atom]
  )