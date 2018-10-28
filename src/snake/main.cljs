(ns snake.main
    (:require
      [snake.core :as core]
      [snake.engine :as engine]
      [reagent.core :as reagent :refer [atom]]
      [snake.app-component :as app-component]))

(enable-console-print!)

(defonce app-state-atom (atom nil))

(add-watch app-state-atom :reagent-state-atom
           (fn [_ _ _ state]
             state))

(swap! app-state-atom
       (fn []
         (core/create-state)))

(defn handle-event!
  [event]
  (condp = (:name event)
    :start-game-clicked
    (println "click")
    (swap! app-state-atom core/start-game (:data event))))

(defn parse-keydown-event
  [event]
  (get {"ArrowUp"    :up
        "ArrowDown"  :down
        "ArrowLeft"  :left
        "ArrowRight" :right}
       (.-key event)))

(defn keydown-handler
  [event]
  (.preventDefault event)
  (swap! app-state-atom core/update-direction (parse-keydown-event event)))


(js/window.addEventListener "keydown" (partial keydown-handler))


(engine/start app-state-atom)

(reagent/render-component [app-component/app-component {:app-state-atom app-state-atom
                                                        :trigger-event (fn [event]
                                                                         (handle-event! event))}]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  (swap! app-state-atom update-in [:__figwheel_counter] inc)
  )