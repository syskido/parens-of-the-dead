(ns undead.memo.components
  (:require [cljs.core.async :refer [put!]]
            [dumdom.core :as d]))

(d/defcomponent Cell [tile reveal-ch]
  [:div {:className "cell"}
    [:div {:className (str "tile"
                            (when (:revealed? tile) " revealed")
                            (when (:matched? tile) " matched"))
            :onClick (fn [e]
                       (.preventDefault e)
                       (put! reveal-ch (:id tile)))}
      [:div {:className "front"}]
      [:div {:className (str "back " (when (:face tile)
                                        (name (:face tile))))}]]])

(d/defcomponent Line [tiles reveal-ch]
  [:div {:className "line"}
   (for [tile tiles]
     [Cell tile reveal-ch])])

(d/defcomponent Board [tiles reveal-ch]
  [:div {:className "board clearfix"}
   (for [four-tiles (partition 4 tiles)]
     [Line four-tiles reveal-ch])])

(d/defcomponent Timer [{:keys [sand index]}]
  [:div {:className (str "timer timer-" index)}
   (for [s sand]
     [:div {:className (str "sand " (name s))}])])

(d/defcomponent Timers [sand]
  [:div {}
   (map-indexed (fn [i s]
                  [Timer {:index i :sand s}])
                (partition 30 sand))])

(d/defcomponent Game [game reveal-ch]
  [:div {:className (when (:foggy? game) "foggy")}
   [Board (:tiles game) reveal-ch]
   [Timers (:sand game)]])

(defn render-game [game container reveal-ch]
  (d/render (Game game reveal-ch) container))
