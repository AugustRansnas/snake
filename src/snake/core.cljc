(ns snake.core
  (:require [ysera.test :refer [is= is is-not]]))

(def game-header "Snejc. Size matters")

(def directions {:right [1 0] :left [-1 0] :down [0 1] :up [0 -1]})

(defn create-snake
  []
  [[5 20] [6 20] [7 20] [8 20]])

(defn create-food
  []
  [(rand-int 79) (rand-int 39)])

(defn temp-food
  []
  [79 ])

;; [0 0] vänstra hörnet uppe
;; [79 0] högra hörnet uppe
;; [0 39] vänstra hörnet nere
;; [79 39] högra hörnet nere

;; y -1 & 40
;; x -1 & 80

;; [- 1 y]
;; [80 y]
;; [x -1]
;; [x 40]



(defn create-state
  []
  {:game-header game-header
   :snake       (create-snake)
   :direction   (:right directions)
   :food        (create-food)
   :game-size   {:width 800
                 :height 400}
   :game-state  "idle"})

(defn get-snake-coordinates
  [state]
  (:snake state))

(defn get-food-coordinates
  [state]
  (:food state))

(defn crash?
  [state]
  (let [snake-head (last (:snake state))
        x (first snake-head)
        y (last snake-head)]
    (or (= x -1)
        (= y -1)
        (= x 80)
        (= y 40))))

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

(defn should-update-direction?
  {:test (fn []
           (is= (should-update-direction? (create-state) :left)
                false)
           (is= (should-update-direction? (create-state) :up)
                true)
           (is= (should-update-direction? (create-state) :right)
                false))}
  [state direction]
  (if (= (:direction state) (get-direction-coordinates direction))
    false
    (condp = direction
      :right
      (not= (:direction state) (get-direction-coordinates :left))
      :left
      (not= (:direction state) (get-direction-coordinates :right))
      :up
      (not= (:direction state) (get-direction-coordinates :down))
      :down
      (not= (:direction state) (get-direction-coordinates :up)))))

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
                    (move-snake)
                    (move-snake)
                    (get-snake-coordinates))
                [[7 20] [8 20] [9 20] [10 20]]))}
  [state]
  (assoc state :snake (as-> (:snake state) $
                            (conj $ (map + (last $) (:direction state)))
                            (remove (fn [coordinate]
                                      (= coordinate (first $))) $)
                            (into [] $))))


(defn game-is-running?
  [state]
  (= (:game-state state) "active"))


(defn start-stop-game
  [state]
  (if (game-is-running? state)
    (assoc state :game-state "idle")
    (assoc state :game-state "active")))

(defn update-game
  [state]
  (if (not (crash? state))
    (-> state
        (move-snake)
        (maybe-grow-snake))
    (start-stop-game state)))
