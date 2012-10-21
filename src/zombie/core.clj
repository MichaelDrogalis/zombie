(ns zombie.core
  (:require [clj-time.core :as time]
            [clojure.pprint :refer :all]))

(defmacro is-like
  "Produce a new piece of data relaltive to description."
  [description & disparity]
  `(-> ~description ~@disparity))

(defmacro but-it
  "Articulate the differences between the two pieces of data."
  [description & disparities]
  `(-> ~description ~@disparities))

(defmacro but-he
  "Alias for but-it."
  [description & disparities]
  `(but-it ~description ~@disparities))

(defmacro but-she
  "Alias for but-it."
  [description & disparities]
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
        str-length (rand-int 50)
        infinite-char-seq (repeatedly #(+ (rand-int (- high-ascii-char low-ascii-char)) low-ascii-char))]
    (apply str (map char (take str-length infinite-char-seq)))))

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

(defn has-a-different
  "Create a new piece of data with a different attribute. Accepts Numbers and Strings."
  [description attribute]
  (morph (attribute description) description attribute))

(defn has-a-smaller
  "Create a new piece of data with a smaller numeric value for attribute."
  [description attribute]
  (assoc description attribute (+ Integer/MIN_VALUE (int (* (rand) (- (attribute description) Integer/MIN_VALUE))))))

(defn has-a-lesser
  "Alias for has-a-smaller."
  [description attribute]
  (has-a-smaller description attribute))

(defn has-no
  "Create a new piece of data with an empty value for attribute. Empty string for strings,
   zero for numbers, and [] for vectors."
  [description attribute]
  (identity-element (attribute description) description attribute))

(defn has-a-nil
  "Create a new piece of data with nil for the attribute."
  [description attribute]
  (assoc description attribute nil))

(defn has-a
  "Create a new piece of data with value for attribute."
  [description attribute value]
  (assoc description attribute value))

(defn birth-with-new-time
  "Create a new piece of data with a concrete time defined by the function f and units."
  [description attribute f units]
  (assoc description attribute (f (attribute description) (units 1))))

(defn has-an-earlier
  "Create a new piece of data with an arbitrarily earlier DateTime for attribute (further in past)."
  [description attribute]
  (birth-with-new-time description attribute time/minus time/months))

(defn has-a-later
  "Create a new piece of data with an arbitrarily later DateTime for attribute (further in future)."
  [description attribute]
  (birth-with-new-time description attribute time/plus time/months))

(defn has-one-day-later
  "Create a new piece of data with attribute one day in the future than it currently is."
  [description attribute]
  (birth-with-new-time description attribute time/plus time/days))

(defn has-one-week-later
  "Create a new piece of data with attribute one week in the future than it currently is."
  [description attribute]
  (birth-with-new-time description attribute time/plus time/weeks))

(defn shout-data!
  "Writes the concrete data used to standard out."
  [n handles aggregation]
  (println "===================================")
  (println "Test case " n)
  (println "===================================")
  (println (map vector handles aggregation)))

(defmacro spawn-with
  "Given a vector of bindings ([a b c d]), gives access to a var called 'all'. Useful for
   handling anonymously named pieces of data, often called '_'."
  [{:keys [n mode aggregate] :or {n 1 mode :quiet aggregate 'zombies} :as options} bindings & body]
  (def binding-names (flatten (partition 1 2 bindings)))
  `(dotimes [n# ~n]
     (let ~(vec bindings)
       (let [~aggregate (flatten (partition 1 2 ~bindings))]
         ~@body
         (if (= ~mode :loud)
           (shout-data! n# binding-names ~aggregate))))))

