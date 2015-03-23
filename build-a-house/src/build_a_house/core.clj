(ns build-a-house.core)

(def tasks '((purchase_lot 2)
             (design_house 5)
             (get_permit 1 purchase_lot design_house)
             (get_bids 14 purchase_lot design_house)
             (select_subs 2 get_bids)
             (excavate 1 get_permit select_subs)
             (construct_basement 7 excavate)
             (order_windows_doors 3 purchase_lot design_house)
             (get_windows_doors 10 order_windows_doors)
             (frame 12 get_permit select_subs)
             (rough_plumbing 5 frame)
             (rough_electric 3 frame)
             (roof 4 frame)
             (install_windows_doors 7 get_windows_doors rough_plumbing rough_electric)
             (vapor_barrier_insulation 2 roof install_windows_doors)
             (drywall 5 vapor_barrier_insulation)
             (inside_paint 3 drywall)
             (cupboards 3 inside_paint)
             (carpet_floor 5 inside_paint)
             (lights 2 inside_paint)
             (plumbing_heating 6 inside_paint)
             (siding 2 roof install_windows_doors)
             (outside_paint 3 siding)
             (move_house 1 cupboards carpet_floor lights plumbing_heating outside_paint)
             (connections 2 construct_basement move_house)
             (landscape 4 construct_basement move_house)
             (move_in 0 landscape connections)))

(defn sum
  "takes the list of tasks as an argument then returns
  the sum of all of the days needed for the individual jobs."
  [tasklist]
  (apply + (map #(nth % 1) tasklist)))

(defn predecessors
  "takes a specific job and the list of tasks then returns
  a list of the immediate predecessors for that job."
  [job tasklist];; handle job not found
  (cond (empty? tasklist) '()
        (= job (first (first tasklist))) (rest (rest (first tasklist)))
        :else (predecessors job (rest tasklist))))

;; Does this include the time it takes to complete pre-requisites?
(defn gettime
  "takes a job and the list of tasks then returns the time that job takes"
  [job tasklist]
  (if (= job (first (first tasklist)))
    (nth (first tasklist) 1)
    (gettime job (rest tasklist))))

(defn build-dependencies
  "Recursively build dependencies using the passed in function"
  [jobs result tasklist fctn]
  (cond (empty? jobs) result
        :else (build-dependencies
               (concat (rest jobs) (filter #(not (or (.contains jobs %)
                                                     (.contains result %)))
                                           (fctn (first jobs) tasklist)))
               (cons (first jobs) result)
               tasklist
               fctn)))

(defn get_all_preds
  "takes a specific job and the list of tasks then returns
  a list of all of the predecessors for that job"
  [job tasklist]
  (let [initpreds (predecessors job tasklist)]
    (build-dependencies initpreds '() tasklist predecessors)))

(defn precedes
  "takes two specific jobs and the list of tasks then returns true if the first job must precede the other and nil otherwise"
  [job1 job2 tasklist]
  (if (.contains (get_all_preds job2 tasklist) job1)
    true))

(defn start_day
  "takes a specific job and the list of tasks then returns the day that this job can start"
  [job tasklist]
                                        ; Add one to provide the first day available
  (+ 1 (apply + (map #(gettime % tasklist) (get_all_preds job tasklist)))))

(defn get_max
  "takes a list of job names and the list of tasks then returns a list with the time and the job that finishes at the greatest time"
  [jobs tasklist]
  ((fn worker
     [working-from result]
     (cond (empty? working-from) result
           :else (let [curr-max (start_day (first working-from) tasklist)]
                   (if (> curr-max (first result))
                     (worker (rest working-from) (cons curr-max (list (first working-from))))
                     (worker (rest working-from) result)))))
   (rest jobs) (list (start_day (first jobs) tasklist) (first jobs))))

(defn critical_path
  "takes a job and the list of tasks then returns a list of the jobs on the critical path to getting this job done in the least amount of time. i.e the result of the following is true (= (get_all_preds 'install_windows_doors) (rest (critical_path 'vapor_barrier_insulation tasks)))"
  [job tasklist]
  (-> job
      (predecessors tasklist)
      (get_max tasklist)
      (last)
      (#(cons % (get_all_preds % tasklist)))))

(defn depends_on
  "takes a job and the list of tasks then returns a list of the jobs that cannot be started until this job has completed. This should return all future jobs, not just the ones immediately affected."
  [job tasklist]
  (map #(first %) (filter #(.contains (rest %) job) (map #(cons (first %) (rest (rest %))) tasklist))))

(defn depends_on_all
 "Not sure if the depends_on implementation is supposed to be the one seen here or the above"
 [job tasklist]
 (build-dependencies (depends_on job tasklist) '() tasklist depends_on))

(defn test-sum
  []
  (str "The sum of all jobs is " (sum tasks) "\n"))

(defn test-predecessors
  []
  (loop [tasklist tasks
         result ""]
    (if (empty? tasklist)
      result
      (recur (rest tasklist)
             (str result
                  "The predecessors for "
                  (first (first tasklist))
                  " are "
                  (predecessors (first (first tasklist)) tasks) "\n")))))

(defn test-gettime
  []
  (loop [tasklist tasks
         result ""]
    (if (empty? tasklist)
      result
      (recur (rest tasklist)
             (str result
                  "The time for "
                  (first (first tasklist))
                  " is "
                  (gettime (first (first tasklist)) tasks) "\n")))))

(defn test-get-allpreds
  []
  (loop [tasklist tasks
         result ""]
    (if (empty? tasklist)
      result
      (recur (rest tasklist)
             (str result
                  "All of the predecessors for "
                  (first (first tasklist))
                  " are "
                  (get_all_preds (first (first tasklist)) tasks) "\n")))))

(defn test-precedes
  []
  (-> (str "Testing to determine if 'construct_basement must precede 'landscape: "
           (precedes 'construct_basement 'landscape tasks)
           "\n")
      (str "Testing to determine if 'landscape must precede 'construct_basement: "
           (precedes 'landscape 'construct_basement tasks)
           "\n")
      (str "Testing to determine if 'install_windows_doors must precede 'vapor_barrier_insulation: "
           (precedes 'install_windows_doors 'vapor_barrier_insulation tasks)
           "\n")
      (str "Testing to determine if 'rough_electric must precede 'vapor_barrier_insulation: "
           (precedes 'rough_electric 'vapor_barrier_insulation tasks)
           "\n")
      (str "Testing to determine if 'design_house must precede 'purhcase_lot: "
           (precedes 'design_house 'purchase_lot tasks)
           "\n")))

(defn test-start-day
  []
  (loop [tasklist tasks
         result ""]
    (if (empty? tasklist)
      result
      (recur (rest tasklist)
             (str result
                  "Job "
                  (first (first tasklist))
                  " can start on  "
                  (start_day (first (first tasklist)) tasks) "\n")))))

(defn test-get-max
  []
  (-> (str "Testing "
           (into '()  (map #(first %) tasks))
           " to determine the job with the greatest length: "
           (get_max (map #(first %) tasks) tasks)
           "\n")
      (str "Testing "
           (into '()  (take-nth 2 (map #(first %) tasks)))
           " to determine the job with the greatest length: "
           (get_max (take-nth 2 (map #(first %) tasks)) tasks)
           "\n")
      (str "Testing "
           (into '()  (take-nth 1 (map #(first %) tasks)))
           " to determine the job with the greatest length: "
           (get_max (take-nth 1 (map #(first %) tasks)) tasks)
           "\n")
      (str "Testing "
           (into '() (drop-last (quot (count tasks) 2) (map #(first %) tasks)))
           " to determine the job with the greatest length: "
           (get_max (drop-last (quot (count tasks) 2) (map #(first %) tasks)) tasks)
           "\n")
      (str "Testing "
           (into '() (drop (quot (count tasks) 2) (map #(first %) tasks)))
           " to determine the job with the greatest length: "
           (get_max (drop (quot (count tasks) 2) (map #(first %) tasks)) tasks)
           "\n")))

(defn test-critical-path
  []
  (loop [tasklist tasks
         result ""]
    (if (empty? tasklist)
      result
      (recur (rest tasklist)
             (str result
                  "The jobs on the critical path to getting "
                  (first (first tasklist))
                  " done on time is: "
                  (critical_path (first (first tasklist)) tasks) "\n")))))

(defn test-depends-on
  []
  (loop [tasklist tasks
         result ""]
    (if (empty? tasklist)
      result
      (recur (rest tasklist)
             (str result
                  "Job "
                  (first (first tasklist))
                  " is depended on by: "
                  (into '() (depends_on (first (first tasklist)) tasks)) "\n")))))

(defn test-depends-on-all
  []
  (loop [tasklist tasks
         result ""]
    (if (empty? tasklist)
      result
      (recur (rest tasklist)
             (str result
                  "Job "
                  (first (first tasklist))
                  " is depended on by ALL of the following: "
                  (depends_on_all (first (first tasklist)) tasks) "\n")))))

(defn -main
  []
  (println "********* Testing sum **********\n" (test-sum))
  (println "********* Testing predecessors **********\n" (test-predecessors))
  (println "********* Testing gettime **********\n" (test-gettime))
  (println "********* Testing get_all_preds **********\n" (test-get-allpreds))
  (println "********* Testing precedes **********\n" (test-precedes))
  (println "********* Testing start_day **********\n" (test-start-day))
  (println "********* Testing get_max **********\n" (test-get-max))
  (println "********* Testing critical_path **********\n" (test-critical-path))
  (println "********* Testing depends_on  **********\n" (test-depends-on))
  (println "********* Testing depends_on_all **********\n" (test-depends-on-all)))
