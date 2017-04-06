(ns user
  (:require [system.repl :as system]
            [kafka.systems :refer [dev-system]]))

(system/set-init! #'dev-system)
