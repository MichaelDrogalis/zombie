(ns zombie.core)

(defmacro is-like [description & disparity]
  `(-> ~description ~@disparity))

(defmacro but-it [description & disparities]
  `(-> ~description ~@disparities))

(defprotocol Differentiate
  (birth-a-different [this description attribute])
  (zero-out [this description attribute]))

(extend-type Number
  Differentiate
  (birth-a-different
   [this description attribute]
   (assoc description attribute (inc (attribute description))))
  (zero-out
   [this description attribute]
   (assoc description attribute 0)))

(extend-type String
  Differentiate
  (birth-a-different
   [this description attribute]
   (assoc description attribute (str (attribute description) "x")))
  (zero-out
   [this description attribute]
   (assoc description attribute "")))

(defn has-a-different [description attribute]
  (birth-a-different (attribute description) description attribute))

(defn has-no [description attribute]
  (zero-out (attribute description) description attribute))

(defn has-a-nil [description attribute]
  (assoc description attribute nil))