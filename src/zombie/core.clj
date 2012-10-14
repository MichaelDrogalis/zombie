(ns zombie.core
  (:require [clj-time.core :as time]
            [clojure.pprint :refer :all]))

(defmacro is-like [description & disparity]
  "Produce a new piece of data relaltive to description."
  `(-> ~description ~@disparity))

(defmacro but-it [description & disparities]
  "Articulate the differences between the two pieces of data."
  `(-> ~description ~@disparities))

(defmacro but-he [description & disparities]
  `(but-it ~description ~@disparities))

(defmacro but-she [description & disparities]
  `(but-it ~description ~@disparities))

(defprotocol Morph
  "Protocol for how to birth new values from old ones"
  (morph [this description attribute])
  (identity-element [this description attribute]))

(defn random-integer []
  (+ Integer/MIN_VALUE (bigint (* (rand) (+ 1 (- Integer/MAX_VALUE Integer/MIN_VALUE))))))

(defn any-integer-but [n]
  (let [k (random-integer)]
    (if (not= n k)
      k
      (recur n))))

(extend-type Number
  Morph
  (morph
   [this description attribute]
   (assoc description attribute (any-integer-but (attribute description)))) 
  (identity-element
   [this description attribute]
   (assoc description attribute 0)))

(defn random-string []
  (let [low-ascii-char 33
        high-ascii-char 126
        str-length (rand-int 50)]
    (apply str
           (map char
                (take str-length (repeatedly
                                  #(+
                                    (rand-int
                                     (- high-ascii-char low-ascii-char))
                                    low-ascii-char)))))))

(defn any-string-but [s]
  (let [r (random-string)]
    (if (not= s r)
      r
      (recur s))))

(extend-type String
  Morph
  (morph
   [this description attribute]
   (assoc description attribute (any-string-but (attribute description))))
  (identity-element
   [this description attribute]
   (assoc description attribute "")))

(extend-type clojure.lang.PersistentVector
  Morph
  (identity-element
   [this description attribute]
   (assoc description attribute []))) 

(defn has-a-different [description attribute]
  "Create a new piece of data with a different attribute. Accepts Numbers and Strings."
  (morph (attribute description) description attribute))

(defn has-a-smaller [description attribute]
  "Create a new piece of data with a smaller numeric value for attribute."
  (assoc description attribute (+
                                Integer/MIN_VALUE
                                (int (* (rand)
                                        (- (attribute description)
                                           Integer/MIN_VALUE))))))

(defn has-a-lesser [description attribute]
  (has-a-smaller description attribute))

(defn has-no [description attribute]
  "Create a new piece of data with an empty value for attribute. Empty string for strings,
   zero for numbers, and [] for vectors."
  (identity-element (attribute description) description attribute))

(defn has-a-nil [description attribute]
  "Create a new piece of data with nil for the attribute."
  (assoc description attribute nil))

(defn has-a [description attribute value]
  "Create a new piece of data with value for attribute."
  (assoc description attribute value))

(defn birth-with-new-time [description attribute f units]
  (assoc description attribute (f (attribute description) (units 1))))

(defn has-an-earlier [description attribute]
  "Create a new piece of data with an arbitrarily earlier DateTime for attribute (further in past)."
  (birth-with-new-time description attribute time/minus time/months))

(defn has-a-later [description attribute]
  "Create a new piece of data with an arbitrarily later DateTime for attribute (further in future)."
  (birth-with-new-time description attribute time/plus time/months))

(defn has-one-day-later [description attribute]
  "Create a new piece of data with attribute one day in the future than it currently is."
  (birth-with-new-time description attribute time/plus time/days))

(defn has-one-week-later [description attribute]
  "Create a new piece of data with attribute one week in the future than it currently is."
  (birth-with-new-time description attribute time/plus time/weeks))

(defn generated-exprs [exprs]
  (vec (concat exprs (vec ['all (into [] (take-nth 2 (drop 1 exprs)))]))))

(defmacro spawn [{:keys [n mode] :or {n 1 mode :quiet} :as options} exprs & body]
  "Given a vector of pairs ([a b c d]), gives access to a var called 'all'. Useful for
   handling anonymously named pieces of data, often called '_'."
  `(dotimes [n# ~n]
     (let ~(generated-exprs exprs)
       ~@body
       (if (= ~mode :loud)
         (do
           (println "---------------")
           (pprint (str "Test case " n#))
           (println "---------------")
           (pprint ~'all))))))
  