;; Not sure why this was here? See note in assoc-playground
; (ns clojure.repl)

(ns clojure-playground.general-language-playground)

;;
; PRINTING STRINGS
;;
(print-str "Aubrey said, \"I think we should go to the Orange Julius.\"")

;;
; VAR BINDING
; ASSIGNMENT USING DEF
;
; ! Don't use this method in a function! Use let instead (see below)
;;
(def my-number 234)
(type #'user/my-number)
; this is the global version?
my-number
; user is a namespace?
; "A namespace will have a new binding from Symbol to Var when the def function is used.
; Then each Var will have its value."
; From here: http://clojurebridge.github.io/community-docs/docs/clojure/def/
user/my-number

;;
; USER DEFINED FUNCTIONS
; you can also bind user defined functions:
;;
(def ten-times (fn [x] (* 10 x)))
(ten-times 6)
; if you can't remember whether you created a var or not:
(resolve 'ten-times)
(resolve 'doesnt-exist)
;; for more on this, see the examples at http://clojurebridge.github.io/community-docs/docs/clojure/def/

(def feet 5)
(def inches 4)

;;
; ARITHMETIC EXERCISE
; basic arithmetic exercise - feet, inches and centimetres
;;
(def feet 5)
(def inches 4)
(def inches-per-foot 12)
(def inches-total (+ (* feet inches-per-foot) inches))
inches-total

(def height-cm (* inches-total 2.54))
height-cm

(def john-height-cm 170)
(def lisa-height-cm 150)
(def average-height (/ (+ clare-height-cm john-height-cm lisa-height-cm) 3))
average-height

; define a function to do this
(def height-in-cm (fn [feet inches] (* 2.54 (+ inches (* 12 feet)))))
(height-in-cm 5 4)

;; COLLECTIONS
; We only look at maps and vectors in this workshop, but there are also the following:
; vectors
; maps
; lists
; StructMaps (now normally replaced by records)
; ArrayMaps
; Sets
; reference: https://clojure.org/reference/data_structures#Collections

;;
; VECTORS
;;
; creating a vector with no name:
(vector 5 10 15)
; adding a new element to an existing vector
; conj stands for conjoin
(conj [3 7] 0)
;;
; VECTOR EXTRACTION
; There are four functions that can do this: nth, count, first, rest
;
; count gives us a count of the number of items in a vector.
(count [5 10 15])
;
; nth gives us the nth item in the vector.
; Note that we start counting at 0, so in the example, calling nth with the number 1
; gives us what we’d call the second element when we aren’t programming.
(nth [5 10 15] 1)
;
; first returns the first item in the collection.
(first [5 10 15])
;
; rest returns all except the first item.
(rest [5 10 15])
;
; vector containing temps for next seven days starting sunday
(vector [25.2 24.3 21.3 16.5 17.4 18 19])
; temp on tuesday
(nth [25.2 24.3 21.3 16.5 17.4 18 19] 2)
; !! This does NOT create a var containing the vector of temps!
(def temps (vector [25.2 24.3 21.3 16.5 17.4 18 19]))
; It is actually a vector containing a vector
temps
; ... so this will work
(nth temps 0)
; ... but this will give you index out of range error
(nth temps 2)
; Instead, you need to do this:
(def temps [25.2 24.3 21.3 16.5 17.4 18 19])
(nth temps 2)

;;
; MAPS
;;
; maps are basically dictionaries
{:first "Sally" :last "Brown"}
; they can hold mixed data types:
{:a 1 :b "two"}
; empty map
{}
; map of a map (ie a dictionary of dictionaries)
{:trinity {:length 40} :clare {:length 30}}
; assoc and dissoc are paired functions: they associate and disassociate items from a map.
(assoc {:first "Sally"} :last "Brown")
(dissoc {:first "Sally" :last "Brown"} :last)
; merge merges two maps together to make a new map.
(merge {:first "Sally"} {:last "Brown"})
;;
; MAP EXTRACTION
(count {:first "Sally" :last "Brown"})
; We can use a keyword like using a function in order to look up values in a map.
(:first {:first "Sally" :last "Brown"})
(:clare {:trinity {:length 40} :clare {:length 30}})
; we can supply a default key to be used when the key we asked for is not in the map.
; (you can call it what you like)
(:last {:first "Sally"} :MISS)
(:last {:first "Sally"} :DEFAULT)
(:last {:first "Sally"} :WHATEVER-YOU-LIKE)
; Then we have keys and vals, which are pretty simple: they return the keys and values in the map.
; The order is not guaranteed, so we could have gotten (:first :last) or (:last :first).
(keys {:first "Sally" :last "Brown"})
(vals {:first "Sally" :last "Brown"})
; get-in will get a particular member
(def st {:trinity {:x -1.7484556000744965E-6, :y 39.99999999999996, :angle 90, :color [106 40 126]}})
(get-in st [:trinity :angle])
;;
; MAP UPDATE
; update-in is a weird one - it looks weiord until you understand that its arguments are a map, a key or list of keys,
; and then a function and the arguments to the function.
; That function and its arguments will be applied to the value associated with the key(s)
; In the example below, it helps to understand that str is effectively a string-concat function
(str "hello, " "world")
(def hello {:count 1 :words "hello"})
(update-in hello [:words] str ", world")
(def mine {:pet {:age 5 :name "able"}})
(update-in mine [:pet :age] - 3)

;;
; COLLECTIONS OF COLLECTIONS
; You can vectors of maps, maps of vectors, etc
{:trinity {:x -1.7484556000744965E-6, :y 39.99999999999996, :angle 90, :color [106 40 126]}}

;;
; DEFINING FUNCTIONS
; this is the original syntax
(def height-in-cm (fn [feet inches] (* 2.54 (+ inches (* 12 feet)))))
(height-in-cm 5 4)
; ... but this is used so often that it now has a shortcut: defn
(defn height-in-cm
  "Given feet and inches as separate values, converts to height in cm - for instance (height-in-cm 5 4) would give 162.56cm for 5'4\""
  [feet inches]
   (* 2.54 (+ inches (* 12 feet))))
; now call the function
(height-in-cm 5 4)
(height-in-cm 5 6)
;;
; PREDICATE FUNCTIONS
(= 3 4)
(>= 5 4)

;;
; HIGHER-ORDER FUNCTIONS
; Functions that take functions as arguments
;
; map function
; map takes a collection and a function
; and applies the function to all the members of the collection
;
; This example applies the partial function, using the + function
; The partial function takes a function f (which is + in this case)
; and fewer than the normal arguments to f, and
; returns a fn that takes a variable number of additional args. When
; called, the returned function calls f with args + additional args.
; So in this example, partial + 90 will apply the + function with an argument of 90,
; with the second argument being (in each case) a member from the collection.
; So what it's actually doing is adding 90 to every member of the collection.
(map (partial + 90) [0 30 60 90])
;
; reduce function
; reduce is kind of the opposite of map
; it takes a collection and reduces it to a single value
;
; For some functions (eg +) it can seem as though reduce does this:
; applies a function that takes multiple arguments, where every member
; of the collection becomes one of the arguments
;
; ... but really it does this: reduce takes the first two members of the provided
; collection and calls the provided function with those members.
; Next, it calls the provided function again–this time, using the result of the previous
; function call, along with the next member of the collection.
; reduce does this over and over again until it finally reaches the end of the collection.
;
; In this example, the + function is applied to every member of the collection
; - so basically we're adding all the members together
(reduce + [30 60 90])
;
; In this example, we apply the str function - which is basically string-concat -
; pasing in every member of the collection, so basically we are concatenating
; all the strings into one giant string.
(reduce str ["clare" " likes" " swimming"])
(reduce str (map name [:clare :likes :swimming]))
(reduce (fn [a b] (str a " " b)) (map name [:clare :likes :swimming]))

;;
; ANONYMOUS FUNCTIONS
;
; an anonymous function
(fn [s1 s2] (str s1 " " s2))
; an anonymous function in situ
(reduce (fn [a b] (str a ", " b)) ["one" "two" "three"])

;;
; ASSIGNMENT USING LET
;
; When you are creating functions, you may want to assign names to values in order to
; reuse those values or make your code more readable. Inside of a function,
; however, you should not use def, like you would outside of a function.
; Instead, you should use a special form called let.
(let [mangoes 3
      oranges 5]
  (+ mangoes oranges))
; inside a function:
(defn opposite
  "Given a collection of turtle names, moves two of them in different directions."
  [names]
  (let [t1 (first names)
        t2 (last names)]
    (forward t1 40)
    (backward t2 30)))

(doc +)

var string = print-str("Hello world")
(print-str "Hello, World!")
var result = 3 + 4

;; REQUIRE
;; When requiring, need single quote:
(require 'clojure.java.io)