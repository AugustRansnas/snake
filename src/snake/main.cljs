(ns snake.main
    (:require
      [snake.core :as core]
      [reagent.core :as reagent :refer [atom]]
      [snake.app-component :as app-component]))

(enable-console-print!)

(defonce app-state-atom (atom nil))

(add-watch app-state-atom :reagent-state-atom
           (fn [_ _ _ state]
             state))

(swap! app-state-atom
       (fn []
         (core/create-state [15 10])))

(defn handle-event!
  [event]
  (condp = (:name event)
    :start-game-clicked
    (swap! app-state-atom core/start-game (:data event))))

(reagent/render-component [app-component/app-component {:app-state-atom app-state-atom
                                                        :trigger-event (fn [event]
                                                                         (handle-event! event))}]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  (swap! app-state-atom update-in [:__figwheel_counter] inc)
  )