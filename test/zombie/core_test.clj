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

(fact
 (let [mike {:friends 5}
       owen (is-like mike (but-it (has-no :friends)))]
   (:friends owen))
   => 0)

(fact
 (let [mike {:name "Mike"}
       pet-rock (is-like mike (but-it (has-no :name)))]
   (empty? (:name pet-rock)))
 => true)

(fact
 (let [bill {:linen-pants-collection [:gray, :blue, :green]}
       mike (is-like bill (but-it (has-a-nil :linen-pants-collection)))]
   (nil? (:linen-pants-collection mike)))
 => true)
