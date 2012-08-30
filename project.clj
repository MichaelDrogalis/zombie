(defproject zombie "0.1.0"
  :description "Clojure test data specifications that won't make you want to punch a window."
  :url "https://github.com/MichaelDrogalis/zombie"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :repositories {"stuart" "http://stuartsierra.com/maven2"}  
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [midje "1.4.0"]
                 [com.stuartsierra/lazytest "1.2.3"]
                 [clj-time "0.4.4"]]
  :profiles {:dev {:plugins [[lein-swank "1.4.4"]
                             [lein-midje "2.0.0-SNAPSHOT"]]}})
  
