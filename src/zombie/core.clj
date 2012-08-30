(ns zombie.core)

(defmacro is-like [description & disparity]
  `(-> ~description ~@disparity))

(defmacro but-it [description & disparities]
  `(-> ~description ~@disparities))

(defprotocol Differentiate
  (birth-a-different [this description attribute]))

(extend-type Number
  Differentiate
  (birth-a-different
   [this description attribute]
   (assoc description attribute (inc (attribute description)))))

(extend-type String
  Differentiate
  (birth-a-different
   [this description attribute]
   (assoc description attribute (str (attribute description) "x"))))

(defn has-a-different [description attribute]
  (birth-a-different (attribute description) description attribute))

