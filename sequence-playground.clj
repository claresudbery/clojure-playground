;; There are many functions that interact on sequences.
;; For example, doseq, dotimes, for, loop, doall, or dorun.

;; doseq
;; The function repeatedly evaluates given body form with each element in the given sequence.
;; The doseq function takes bindings as arguments. The arguments might be an odd-looking vector: [name sequence].
;; When each element of a sequence is iterated over, the element is assigned to the name one by one.
;; !! Unfortunately doseq always returns nil, which is all teh output I'm seeing in the repl
;; when I run this. I can't see any of the print-str output!
(def names (vector "clare" "ally" "felix" "oscar"))
(def people (atom ["nobody"]))
(print-str (str "people: " @people))
;; This will cycle through the list of names and add them all one by one to the people atom:
(doseq [n names] (swap! people assoc (count @people) n))
(print-str (str "people: " @people))


;; dotimes
;; Like doseq, the function repeatedly evaluates given body form.
;; The difference is the binding in the argument.
;; The dotimes takes: [name max-integer].
;; The dotimes function is the closest to so-called for-loop in other programming languages.
;; This function allows us an index access to the given sequence by a combination with nth.
;; !! Unfortunately doseq always returns nil, which is all teh output I'm seeing in the repl
;; when I run this. I can't see any of the print-str output!
(def names (vector "clare" "ally" "felix" "oscar"))
(def people (atom ["nobody"]))
(print-str (str "people: " @people))
;; This will cycle through the list of names and add them all one by one to the people atom:
(dotimes [n (count names)] (swap! people assoc n (nth names n)))
(print-str (str "people: " @people))


;; for
(def digits (seq [1 2 3]))
(for [x digits] (* x 10))
;= (10 20 30)


;; loop (AND RECURSION)
;; More on loop recur here: https://clojurebridge.github.io/community-docs/docs/clojure/recur/
;; And here: https://programming-pages.com/2012/01/23/loops-in-clojure/
;;    ... but to get that one, you probably need to read this one first: https://programming-pages.com/2012/01/16/recursion-in-clojure/
;; Here is a recursive function using tail recursion:
(defn power
  ([x y] (power x y 1))
  ([x y current]
  (if (= y 0)
    current
    (if (> y 0)
      (recur x (- y 1) (* x current))
      (recur x (+ y 1) (/ current x))))))
;; ... and here is that function rewritten using the loop keyword, which basically is shorthand for the same thing.
;; The loop function allows us to merge the initialization step into the recursive part of the function,
;; so that only one argument list is required, rather than the two we used above.
(defn power
  [x y]
  (loop [exponent y
         current 1.0]
    (if (= exponent 0)
      current
      (if (> exponent 0)
        (recur (- exponent 1) (* x current))
        (recur (+ exponent 1) (/ current x))))))


;; doall
;; When lazy sequences are produced via functions that have side
;; effects, any effects other than those needed to produce the first
;; element in the seq do not occur until the seq is consumed. doall can
;; be used to force any effects. Walks through the successive nexts of
;; the seq, retains the head and returns it, thus causing the entire
;; seq to reside in memory at one time.
;; Nothing is printed because map returns a lazy-seq:
(def foo (map println [1 2 3]))
#'user/foo
;; doall forces the seq to be realized:
(def foo (doall (map println [1 2 3])))
;= 1
;= 2
;= 3
;; where
(doall (map println [1 2 3]))
;= 1
;= 2
;= 3
;= (nil nil nil)


;; dorun
;; Same as doall but returns nil instead of returning the sequence.
