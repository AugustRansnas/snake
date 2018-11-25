(ns snake.engine
  (:require
    [cljs.core.async :as async]
    [snake.core :as core]))

(def tick-speed 50)


(defn game-loop
  [app-state-atom]
  (async/go-loop
    []
    (async/<! (async/timeout tick-speed))
    (if (core/game-is-running? @app-state-atom)
      (swap! app-state-atom core/update-game) nil)
    (recur)))


(defn start
  [app-state-atom]
  (game-loop app-state-atom))