(ns snake.core
  (:require [ysera.test :refer [is= is is-not]]))

(def game-header "Snake. Size matters")

(defn create-snake
  []
  [[5 20] [6 20] [7 20] [8 20]])

(defn create-state
  []
  {:game-header game-header
   :snake       (create-snake)
   :direction   "right"
   :game-state  "active"})




(defn get-snake-coordinates
  [state]
  (:snake state))

(defn update-snake-position
  {:test (fn []
           (is= (-> (create-state)
                    (update-snake-position)
                    (get-snake-coordinates))
                [[41 20] [42 20] [43 20] [44 20]])
           )}
  [state]
  (assoc state :snake  (map (fn [coordinate]
                                   [(inc (first coordinate)) (last coordinate)]
                                   )
                                 (get-snake-coordinates state))))


(defn game-is-running?
  [app-state-atom]
  (= (:game-state app-state-atom) "active"))


(defn start-game
  [state]
  (if (not (game-is-running? state))
    (assoc state :game-state "active")
    (assoc state :game-state "idle")))

(defn update-game
  [state]
  (update-snake-position state))