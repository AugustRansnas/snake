(ns snake.core
  (:require [ysera.test :refer [is= is is-not]]))

(defn create-board
  "creates the game board i.e valid positions for the snake"
  {:test (fn []
           (is= (create-board [4 3])
                #{[0 0] [0 1] [0 2] [1 0] [1 1] [1 2] [2 0] [2 1] [2 2] [3 0] [3 1] [3 2]}
                ))}
  [[x y]]
  (->> (for [x-pos (range x)
             y-pos (range y)]
         [x-pos y-pos])
       (into #{}))
  )

(defn create-state
  {:test (fn []
           (is= (create-state [4 3])
                {:board (create-board [4 3])
                 :snake #{}
                 }
                ))}
  [[x y]]
  {:board (create-board [x y])
   :snake #{}
   }
  )
