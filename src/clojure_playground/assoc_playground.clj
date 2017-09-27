;; Not sure why I had this, but what it would actually means
;; is that everything in this file is being added to the core namespace.
;; potentially this could mean I was redefining core stuff!
;; Maybe not such a great iea then...
; (ns clojure.core)

(ns clojure-playground.assoc-playground)

; Define a map whose key is not a keyword
(def m2 {4 11, 3 0})
m2

; Define a map with three levels of nesting:
(def m {:a 3, :b 5, :c {:d {:e 25}}})

; Print out your map for you to see:
m

; Change a value without caring about previous value – throws previous value away
; (depending on your point of view). The other way of describing this is to say that
; the original map is immutable, and unchanged by this statement. This creates a
; brand new map.
(assoc m :a 5)

; Update requires a function – which will receive the previous value and return a new one
; It's still creating a new map though - the original is unchanged.
(update m :a inc)

; Add an additional key (because :f does not exist in the map already)
; (if you want to add a nested key, you need to use assoc-in - see below)
(assoc m :f 6)

; Using assoc on a vector - have to specify the index of the thing being inserted
; if the index is 1 greater than the existing size, the new value is appended
(assoc ["clare"] 1 "ally")
; if the index is more than 1 greater than the existing size, you get an error
(assoc ["clare"] 2 "ally")
; if the index is the index of an existing member, that value gets replaced (so the new value is not actually inserted)
(assoc ["clare" "ally" "oscar"] 2 "felix")
(assoc ["clare" "ally" "oscar"] 3 "felix")

; Remove a key
(dissoc (assoc m :f 6) :f)

; Return a new map where the value of 25 has now been changed to 30
; The difference between assoc and assoc-in is that assoc supports nesting
; Expected result: {:a 3, :b 5, :c {:d {:e 30}}}
m
(assoc-in m [:c :d :e] 30)

; Return a new map – based on the original (which is of course immutable)
; – but now, at the level of e, there will also be a member called f (in the same map)
; Expected result: {:a 3, :b 5, :c {:d {:e 25, :f 30}}}
; So, in the square brackets, the order defines level of nesting
; – the third element in there represents the third level down
(assoc-in m [:c :d :f] 30)

; So this would not reach the existing e, but would instead add a new e at the same level as d:
; The result would look like this: {:a 3, :b 5, :c {:d {:e 25}, :e 30}}
(assoc-in m [:c :e] 30)

; This will create some new nesting, because :g is the fourth member, so we now have a fourth level of nesting
; Note that a new element - :f - is created, and then :g is nested under :f.
; This wouldn't work - (assoc-in m [:c :d :e :f] 30) - because :e is an int, not a map.
m
(assoc-in m [:c :d :f :g] 30)

; Define a vector of maps
(def users [{:name "James", :age 26}  {:name "John", :age 43}])

;; update the age of the second (index 1) user
; Notice how we can reference a map element within a vector element: First we index the vector,
; then specify the map key.
(assoc-in users [1 :age] 44)

; Define a map of vectors
(def users2 {:user1 ["James" 26], :user2 ["John" 43]})

;; update the age of the second user
; Notice how we can now reference a vector element within a map element: First we specify the map key,
; then we index the vector.
(assoc-in users2 [:user2 1] 45)

;; insert the password of the second (index 1) user
(assoc-in users [1 :password] "nhoJ")
;;=> [{:name "James", :age 26} {:password "nhoJ", :name "John", :age 43}]

;; create a third (index 2) user. The following three lines all have the same effect
(assoc users 2 {:name "Jack", :age 19})
(conj users {:name "Jack", :age 19})
(assoc-in users [2] {:name "Jack", :age 19})

; Start with an empty map, add an element whose key is 1 (remember keys don't have to be keywords),
; set that element to have a nested element with a key of :connections and a value which is also a map,
; set the key of that map to 4 and its value to 2
; Both the following lines create the same map (although the first also gives it a definition m)
(def m {1 {:connections {4 2}}})
(assoc-in {} [1 :connections 4] 2)

;; assoc-in can also be used to update a mutable item (in conjunction with swap)
; See mutability-playground for more info
(def ppl (atom {"persons" {"joe" {:age 1}}}))
(swap! ppl assoc-in ["persons" "bob"] {:age 11})
; the @ symbol is used to dereference the atom
@ppl
;;=> {"persons" {"joe" {:age 1}, "bob" {:age 11}}}

;; be careful with that empty path sequence, it's seldom what you want.
;; In general, you find that for a non-empty path
;;   (get-in (assoc-in m path v) path)
;; is equal to v.
;; Surprisingly this does not hold true in case of an empty path.
(assoc-in {} [] {:k :v})
;;=> {nil {:k :v}}

