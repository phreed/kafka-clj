
(ns kafka.core
  (:gen-class)
  (:require [system.repl :as system]
            [kafka.systems :refer [prod-system]]
            [system.components.kafka-server :as server]
            [clojure.pprint :as pp]
            [clojure.tools.logging :as log]))

(defn -main
  "Start the production application"
  []
  (system/set-init! #'prod-system)
  (system/start)
  (server/receive-messages (:kafka-db system/system)
     (fn [item] (log/info "recv function" item))))
