(ns snake.main
    (:require
      [snake.core :as core]
      [reagent.core :as reagent :refer [atom]]
      [snake.app-component :as app-component]))

(enable-console-print!)

(println "This text is printed from src/snake/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state-atom (atom (core/create-state [15 10])))


(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
   (swap! app-state-atom update-in [:__figwheel_counter] inc)
)

(reagent/render-component [app-component/app-component app-state-atom]
                          (. js/document (getElementById "app")))
