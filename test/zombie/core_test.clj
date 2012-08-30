(ns zombie.core-test
  (:require [midje.sweet :refer :all]
            [zombie.core :refer :all]))

(fact
 (let [mike {:age 21}
       bill (is-like mike (but-it (has-a-different :age)))]
   (not= (:age mike) (:age bill)))
 => true)

(fact
 (let [mike {:favorite-color "green"}
       owen (is-like mike (but-it (has-a-different :favorite-color)))]
   (not= (:favorite-color mike) (:favorite-color owen)))
 => true)

