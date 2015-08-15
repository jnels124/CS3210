(ns tic-tac-toe.core)

(def board ["" "1" "2" "3" "4" "5" "6" "7" "8"])

(defn update-game
  [pos choice]
  ())
(defn is-win? [who]
  (or (= who  (board 0) (board 1) (board 2))
      (= who  (board 0) (board 3) (board 6))
      (= who  (board 0) (board 4) (board 8))
      (= who  (board 3) (board 4) (board 5))
      (= who  (board 6) (board 7) (board 8))
      (= who  (board 6) (board 4) (board 2))
      (= who  (board 1) (board 4) (board 7))
      (= who  (board 8) (board 5) (board 2))))

(defn print-board []
  (println (board 0) " | " (board 1) " | "  (board 2))
  (println "-------------")
  (println (board 3) " | " (board 4) " | "  (board 5))
  (println "-------------")
  (println (board 6) " | " (board 7) " | "  (board 8)))

(defn is-available?
  [pos]
  (not (or (== (board pos) \x)
           (== (board pos) \o))))

(defn handle-choice
  [choice]
  (if (or (not (is-available? (read-string choice)))
       (not (instance? java.lang.Number choice))
       (and (choice > 0 ) (choice < 9)))
    (do (println "You have not entered a valid number. Please choose a valid number from the board.")
      (handle-choice (read-string)))
    choice))

(defn player-choice
  []
  (handle-choice (read-string)))

(defn get-available
  [theboard available]
  (if (empty? theboard)
    available
    (if ())))

(defn computer-choice
  []
  (let [available-choices (get-available)]))

(defn play
  []
  ())

(defn start
  []
  (println "Welcome to tic tac toe")
  (print-board)
  (println "Where do you want to move?")
  (play))
