(ns snake.core
  (:require [ysera.test :refer [is= is is-not]]))

(def game-header "August's snake. Size matters.")

(defn create-board
  "Creates da game board i.e valid positions for the snake"
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

(defn create-snake
  "Creates da snake. Size matters."
  {:test (fn [] (is=(create-snake) #{[5 5] [6 5] [7 5] [8 5]}))}
  []
  (into #{[5 5] [6 5] [7 5] [8 5]})
  )

(defn create-state
  {:test (fn []
           (is= (create-state [4 3])
                {:game-header game-header
                 :board (create-board [4 3])
                 :snake (create-snake)
                 }
                ))}
  [[x y]]
  {
   :game-header game-header
   :board (create-board [x y])
   :snake (create-snake)
   :game-state "idle"
   }
  )

(defn start-game
  [state]
  (assoc :game-state state)
  )