(ns zombie.core-test
  (:require [midje.sweet :refer :all]
            [clj-time.core :as time]
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
 (let [greg {:cats [:boots]}
       mike (is-like greg (but-it (has-no :cats)))]
   (:cats mike))
 => [])

(fact
 (let [bill {:linen-pants-collection [:gray, :blue, :green]}
       mike (is-like bill (but-it (has-a-nil :linen-pants-collection)))]
   (nil? (:linen-pants-collection mike)))
 => true)

(fact
 (let [mike {}
       bill (is-like mike (but-it (has-a :car :red)))]
   (:car bill))
 => :red)

(fact
 (let [mike {:birthday (time/date-time 1991 1 13)}
       owen (is-like mike (but-it (has-an-earlier :birthday)))]
   (time/before? (:birthday owen) (:birthday mike)))
 => true)

(fact
 (let [mike {:birthday (time/date-time 1991 1 13)}
       kyle (is-like mike (but-it (has-a-later :birthday)))]
   (time/before? (:birthday mike) (:birthday kyle)))
 => true)
