(ns snake.core
  (:require [ysera.test :refer [is= is is-not]]))

(def game-header "Snejc. Size matters")

(def directions {:right [1 0] :left [-1 0] :down [0 1] :up [0 -1]})

(defn create-snake
  []
  [[5 20] [6 20] [7 20] [8 20]])

(defn create-food
  []
  [(rand-int 30) (rand-int 30)])

(defn create-state
  []
  {:game-header game-header
   :snake       (create-snake)
   :direction   (:right directions)
   :food        (create-food)
   :game-state  "active"})

(defn get-snake-coordinates
  [state]
  (:snake state))

(defn get-food-coordinates
  [state]
  (:food state))

(defn should-grow-snake?
  [state]
  {:test (fn []
           (is= (-> (create-state)
                    (assoc :snake [[5 20] [6 20] [7 20] [8 20]])
                    (assoc :food [25 72]))
                false)
           (is= (-> (create-state)
                    (assoc :snake [[5 20] [6 20] [7 20] [8 20]])
                    (assoc :food [8 20]))
                true))}
  (if (= (last (:snake state)) (:food state)) true false))

(defn maybe-grow-snake
  [state]
  (if (should-grow-snake? state)
    (-> state
        (assoc :snake (cons (map - (first (:snake state)) (:direction state)) (:snake state)))
        (assoc :food (create-food)))
    state))

(defn get-direction-coordinates
  {:test (fn []
           (is= (get-direction-coordinates :right)
                [1 0])
           (is= (get-direction-coordinates :down)
                [0 1]))}
  [direction]
  (get directions direction))

(defn update-direction
  [state direction]
  (assoc state :direction (get-direction-coordinates direction)))

(defn move-snake
  {:test (fn []
           (is= (-> (create-state)
                    (move-snake)
                    (get-snake-coordinates))
                [[6 20] [7 20] [8 20] [9 20]])
           (is= (-> (create-state)
                    (update-direction :down)
                    (move-snake)
                    (get-snake-coordinates))
                [[6 20] [7 20] [8 20] [8 21]])
           (is= (-> (create-state)
                    (update-direction :right)
                    ((fn [state] (println (:snake state)) state))
                    (move-snake)
                    ((fn [state] (println (:snake state)) state))
                    (move-snake)
                    ((fn [state] (println (:snake state)) state))
                    (get-snake-coordinates))
                [[7 20] [8 20] [9 20] [10 20]]))}
  [state]
  (assoc state :snake (as-> (:snake state) $
                            (conj $ (map + (last $) (:direction state)))
                            (remove (fn [coordinate]
                                      (= coordinate (first $))) $)
                            (into [] $))))


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
  (-> state
      (move-snake)
      (maybe-grow-snake)))