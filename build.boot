

(set-env!
 :resource-paths #{"src/res"}
 :source-paths #{"src/clj"}
 :dependencies
   '[;[org.clojure/clojure "1.9.0-alpha15"]

     [org.danielsz/system "0.4.0"]
     [adzerk/boot-reload "0.5.1" :scope "test"]
     [environ "1.1.0"]
     [boot-environ "1.1.0"]
     [org.clojure/tools.namespace "0.3.0-alpha3"]
     ;; https://mvnrepository.com/artifact/ch.qos.logback/logback-classic
     [ch.qos.logback/logback-classic "1.2.3"]

     [proto-repl "0.3.1"]
     [proto-repl-charts "0.3.2"
         :exclusions [org.clojure/tools.namespace]]

     [org.apache.kafka/kafka-clients "0.10.2.0"]
     [org.apache.kafka/kafka-streams "0.10.2.0"]
     [danielsz/boot-shell "0.0.1"]
     [org.clojure/data.json "0.2.6"]
     [com.cognitect/transit-clj "0.8.300"]])

(require
  '[system.boot :refer [system run]]
  '[environ.boot :refer [environ]]
  '[adzerk.boot-reload :refer [reload]]
  '[kafka.systems :refer [dev-system prod-system]])

(deftask proto-repl
 "This task is intended to be used in conjunction with
  the proto-repl plugin for the atom.io editor."
 []
 (set-env! :init-ns 'user
           :source-paths #(conj % "src/clj-dev"))
 (comp
   (environ :env {:thing "that"})
   (watch :verbose true)
   (system :sys #'dev-system
           :auto true)
           ;; :files ["core.clj"])
   (reload)
   (repl :server true)))

(deftask prod
  "This task makes this a stand-alone operation"
  []
  (comp
     (run :main-namespace "kafka.core")))
