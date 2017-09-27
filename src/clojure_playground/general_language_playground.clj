;; Not sure why this was here? See note in assoc-playground
; (ns clojure.repl)

;; require:
;; require statements go inside namespace declarations.
;; This is saying that we will include the let-playground code
;; in this file, and can now qualify that code using "let-stuff"
;; instead of "let-playground"
(ns clojure-playground.general-language-playground
  (:require [clojure-playground.let-playground :as let-stuff]))

;;
; PRINTING STRINGS
;;
(println "Aubrey said, \"I think we should go to the Orange Julius.\"")

;;
; VAR BINDING
; ASSIGNMENT USING DEF
;
; ! Don't use this method in a function! Use let instead (see below)
;;
(def my-number 234)
;;
; find the type of the thing you just defined:
(type my-number)
;;
; print out my-number:
my-number

;;
; USER DEFINED FUNCTIONS
; you can also bind user defined functions:
;;
(def ten-times (fn [x] (* 10 x)))
(ten-times 6)
;;
; if you can't remember whether you created a var or not:
(resolve 'ten-times) ;; this will return the fully-qualified name (if defined)
(resolve 'doesnt-exist) ;; this will return nil
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
(count (vector 5 10 15 20))
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
; vector containing temperatures for next seven days starting sunday
(vector 25.2 24.3 21.3 16.5 17.4 18 19)
; temp on tuesday
(nth [25.2 24.3 21.3 16.5 17.4 18 19] 2)
;;
; !! This does NOT create a var containing the vector of temps!
(def temps (vector [25.2 24.3 21.3 16.5 17.4 18 19]))
; It is actually a vector containing a vector
; The mistake was that as well as using the vector keyword,
; we also used the square bracket notation
; - meaning that we actually defined two vectors, one containing the other
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
; maps are basically dictionaries, or sets of key-value pairs
{:first "Sally" :last "Brown"}
; they can hold mixed data types:
{:a 1 :b "two"}
; empty map
{}
; map of a map (ie a dictionary of dictionaries)
{:trinity {:length 40} :clare {:length 30}}
; assoc and dissoc are paired functions: they associate and disassociate items from a map.
; this will add a new key-value pair to the map defined by {:first "Sally"}:
(assoc {:first "Sally"} :last "Brown")
;;
; This will remove the pair whose key is :last, leaving only onw key-value pair in the map:
(dissoc {:first "Sally" :last "Brown"} :last)
;
; merge merges two maps together to make a new map:
(merge {:first "Sally"} {:last "Brown"})
;;
; MAP EXTRACTION
(count {:first "Sally" :last "Brown"})
; We can use a keyword like using a function in order to look up values in a map.
(:first {:first "Sally" :last "Brown"})
(:clare {:trinity {:length 40} :clare {:length 30 :height 50}})
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
(def st {:trinity {:x -1.7484556000744965E-6, :y 39.99999999999996, :angle 90, :color {:first [106 40 126]}}})
(get-in st [:trinity :angle])
(get-in st [:trinity :color])
; because we have given the value whose key is :color a key called :first, the following two are equivalent:
(:first (get-in st [:trinity :color]))
(get-in st [:trinity :color :first])
; ...but if we actually want the first element in the vector whose key is :first, we use the keyword 'first'
; (not the same thing as the key we have defined as :first):
(first (get-in st [:trinity :color :first]))
;;
; MAP UPDATE
; update-in is a weird one - it looks weird until you understand that its arguments are a map,
; a key or list of keys, and then a function and the arguments to the function.
; That function and its arguments will be applied to the value associated with the key(s)
; In the examples below, it helps to understand that str is effectively a string-concat function
(str "hello, " "world")
; for this example we are only providing one key, but we still need to enclose it in a vector:
(def hello {:count 1 :words "hello"})
(update-in hello [:words] str ", world")
; for this example we are providing more than one key, but it fails because the keys refer to more than one value:
(def greetings0 {:count 1 :phrase1 "hello" :phrase2 "goodbye"})
(update-in greetings0 [:phrase1 :phrase2] str ", world")
; this example works because the provided keys resolve to just one value
; (if you want to apply a function to more than one value, use the map function instead - see below):
(def greetings1 {:count 1 :phrase1 {:phrase2 "goodbye"}})
(update-in greetings1 [:phrase1 :phrase2] str ", world")
; for this example the function is just the subtraction operator
(def mine {:pet {:age 5 :name "able"}})
(update-in mine [:pet :age] - 3)

;;
; COLLECTIONS OF COLLECTIONS
; You can have vectors of maps, maps of vectors, etc
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
; PREDICATE FUNCTIONS (ie those that return booleans)
(= 3 4)
(>= 5 4)

;;
; HIGHER-ORDER FUNCTIONS
; Functions that take functions as arguments
;
; map function
; map takes a collection and a function
; and applies the function to all the members of the collection
; This simple example applies the increment function to all members of the collection:
(map inc [0 30 60 90])
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
; This is similar to the update-in example from above, but this time we want to act on all members:
; (the hash character is the # function macro - for more on this, see here:
; https://yobriefca.se/blog/2014/05/19/the-weird-and-wonderful-characters-of-clojure/)
; (basically we are making an inline function definition - a function whose first argument (denoted by %)
; will be the value from the vector, and whose second argument will be ", world")
(def greetings2 ["hello" "goodbye"])
(map #(str % ", world") greetings2)
; we can also use fn to create an anonymous function:
(map (fn [a] (str a ", world")) greetings2)
; Same again but closer to the original intention - now we use a map instead of a vector,
; and access all values using the vals function
(def greetings3 {:phrase1 "hello" :phrase2 "goodbye"})
(map #(str % ", world" ) (vals greetings3))
; ... and finally we achieve exactly what we originally wanted to achieve, by specifying a subset of keys:
(def greetings4 {:count 6 :phrase1 "hello" :phrase2 "goodbye"})
(map #(str % ", world" ) (vals (select-keys greetings4 [:phrase1 :phrase2])))
; here is another example using the # function macro, because we want to divide everything by 10,
; which means we want to specify the 1st argument rather than the 2nd - which is what we get if we use partial:
(def numbers [10 20 30])
(map #(/ % 10) numbers)
; If we use partial, the arguments to the / function are in the wrong order:
(map (partial / 10) numbers)
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
; Next, it calls the provided function again – this time, using the result of the previous
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
; here we use the name function, which converts a set of key into strings (see below):
(reduce str (map name [:clare :likes :swimming]))
; here we create an anonymous function that concatenates two strings with a space between,
; and we map the name function across a collection of symbols to create a collection of strings,
; and finally we use reduce to apply our anonymous function to the collection of strings
(reduce (fn [a b] (str a " " b)) (map name [:clare :likes :swimming]))

; name function:
; returns the name of a symbol
;
(name :clare)
; here we have a vector whose first element is a symbol
(name (first [:clare 10 :owen 11 :james 40]))
; this causes an error because the second element is not a symbol
; (this example originally happened because I created a vector by mistake when I was trying to create a map):
(name (nth [:clare 10 :owen 11 :james 40] 1))
; this works though, because the third element is a symbol:
(name (nth [:clare 10 :owen 11 :james 40] 2))
; this doesn't work, because the first element of this map is a key-value pair, not a symbol:
(name (first {:clare 10 :owen 11 :james 40}))
; Instead, we take the first element and ask for its key, which is indeed a symbol:
(key (first {:clare 10 :owen 11 :james 40}))
(name (key (first {:clare 10 :owen 11 :james 40})))
; here we take all the keys from a map, then map the name function across all of them:
(map name (keys {:clare 10 :owen 11 :james 40}))

;;
; ANONYMOUS FUNCTIONS
;
; an anonymous function
(fn [s1 s2] (str s1 " " s2))
; an anonymous function in situ
(reduce (fn [a b] (str a ", " b)) ["one" "two" "three"])
;
; using the # macro to create an anonymous function:
#(str % ", elephant")
; in situ (note that this can only be used with map, not with reduce
; - because you can only have one argument to this type of anonymous function, and reduce needs 2 arguments):
(map #(str % ", elephant") ["one" "two" "three"])

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
(defn first-and-last
  "Given a collection of names, tells you the first and last."
  [names]
  (let [t1 (first names)
        t2 (last names)]
    (println  (str "first: " t1)
              ";" (str "last: " t2))))
(first-and-last ["Clare" "John" "Edgar" "Susan"])

;; REQUIRE
;; When requiring, need single quote:
(require 'clojure.java.io)