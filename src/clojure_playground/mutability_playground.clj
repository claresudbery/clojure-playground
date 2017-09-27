(ns clojure-playground.mutability-playground)

;; More on atoms: https://clojure.org/reference/atoms
;; and here: http://personal.morris.umn.edu/~elenam/4651Spring2016/examples/mutability.html

;; assoc-in can be used to update a mutable item
;; See mutability-playground for more info
(def ppl (atom {"persons" {"joe" {:age 1}}}))
(swap! ppl assoc-in ["persons" "bob"] {:age 11})

; define an atom containing a map
(def mymap (atom {:a 1 :b 2}))
@mymap
; access the value whose key is :a
(:a @mymap)

;;	In Signposts, Atom names have exclamation marks – just a stylistic thing
;;	It helps to identify atoms, because if you omit the @ symbol from atoms it simply won’t work, but the error is quite silent


;; using swap on an atom containing a vector
(def names ["one" "two"])
(assoc names 1 "ally")
(def people (atom names))
@people
(swap! people assoc 1 "ally")
@people

;; using swap with update on an atom (Simple atom manipulation:)
(swap! a update :a inc)
;;	equivalent to
(swap! a (fn [val-of-a] (update val-of-a :a (fn [old-a] (inc old-a)))))

;; The next bits come from here: http://personal.morris.umn.edu/~elenam/4651Spring2016/examples/mutability.html

;; atoms are simple mutable items of data
;; changes become immediately visible to all threads,
;; changes are guaranteed to be synchronized by the JVM
(def atom1 (atom 0))

;; dereferencing atom:
(print-str (str "atom1: " @atom1))
;= "atom1: 0"

;; changing a value of an atom: pass a function which will change old value to new one
(swap! atom1 inc)
;= 1
(print-str (str "atom1: " @atom1))
;= "atom1: 1"

;; #(* % 2) is using the function macro which provides shorthand for defining anonymous functions.
;; It's basically an anonymous function which multipilies the input (marked by '%') by 2.
(swap! atom1 #(* % 2))
;= 2
(print-str (str "atom1: " @atom1))
;= "atom1: 2"

;; state of an atom can be any value
(def atom2 (atom {:a 1 :b "hello"}))
(print-str (str "atom2: " @atom2))
;= "atom1: {:a 1, :b \"hello\"}"

(swap! atom2 #(assoc % :b "hi"))
;= {:a 1, :b "hi"}
(print-str (str "atom2: " @atom2))
;= "atom1: {:a 1, :b \"hi\"}"

;; reset, ignore the previous value
(reset! atom1 55)
;= 55
(print-str (str "atom1: " @atom1))
;= "atom1: 55"

;;; Agents:
;; Agents are references that are updated asynchronously:
;; updates happen at a later, unknown point in time, in a thread pool.
(def agent1 (agent 0))
(print-str (str "agent1: " @agent1))
;= "agent1: 0"

(send agent1 inc)
(send agent1 inc)
(Thread/sleep 1000)
(print-str (str "agent1: " @agent1)) ;; may be 0,1,2
;= "agent1: 4"

;; Refs are coordinated references: we can update multiple references
;; in a transaction
;; Transactions have the following properties: atomicity, consistency, isolation, durability
;; Refs are implemented with STM: Software Transactional Memory
(def account1 (ref 500))
(def account2 (ref 0))

(print-str (str "account 1: " @account1))
;= "account 1: 500"
(print-str (str "account 2: " @account2))
;= "account 2: 0"

(defn transfer [acc1 acc2 amount]
  (dosync
    (alter acc1 - amount)
    (alter acc2 + amount)))

(transfer account1 account2 300)
;= 300

(print-str (str "account 1: " @account1))
;= "account 1: 200"
(print-str (str "account 2: " @account2))
;= "account 2: 300"

(transfer account2 account1 100)

(print-str (str "account 1: " @account1))
;= "account 1: 300"
(print-str (str "account 2: " @account2))
;= "account 2: 200"

;; you cannot modify a ref without synchronization:
;(alter account1 + 20)

;; if the order of operations doesn't matter, use commute instead of alter,
;; for efficiency:
(defn payday [acc1 acc2 amount]
  (dosync
    (commute acc1 + 500)
    (commute acc2 + 500)))

(print-str (str "account 1: " @account1))
;= "account 1: 300"
(print-str (str "account 2: " @account2))
;= "account 2: 200"

;;;; Delays, futures and promises: thread handling

;; delay Takes a body of expressions and yields a Delay object that will
;; invoke the body only the first time it is forced (with force or deref/@)
;; Subsequent times the cached result will be returned

;; using delay to get a timestamp:
(def d (delay (System/currentTimeMillis)))
(print-str @d)
(Thread/sleep 1000)
(print-str @d)
;= output is same as previous print


;; Future: evaluates a piece of code in another thread.
;; Returns immediately  (it never blocks the current thread).
(def long-comp (future (reduce + (map inc (range  100000000)))))
(realized? long-comp)
;= false
(print-str @long-comp)
;= "5000000050000000"


;; Promises: don't have code to evaluate, are "holders"
;; for a value placed via deliver function
(def p (promise))
(realized? p)
;= false
(deliver p 42)
(realized? p)
;= true
(+ @p 2)
;= 44
