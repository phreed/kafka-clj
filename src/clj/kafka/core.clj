
(ns kafka.core
  (:gen-class)
  (:require [system.repl :as system]
            [kafka.systems :refer [prod-system]]))

(defn -main
  "Start the production application"
  []
  (system/set-init! #'prod-system)
  (system/start))
