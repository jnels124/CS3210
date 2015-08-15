(ns hw2.core)

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
  [jobs result tasklist]
  (cond (empty? jobs) result
        :else (build-dependencies
           (concat (rest jobs) (filter #(not (or (.contains jobs %)
                                                 (.contains result %)))
                                       (predecessors (first jobs) tasklist)))
           (cons (first jobs) result)
           tasklist)))

(defn get_all_preds
  "takes a specific job and the list of tasks then returns
  a list of all of the predecessors for that job"
  [job tasklist]
  (let [initpreds (predecessors job tasklist)]
    (build-dependencies initpreds '() tasklist)))

(defn precedes
  "takes two specific jobs and the list of tasks then returns true if the first job must precede the other and nil otherwise"
  [job1 job2 tasklist]
  (if (.contains (predecessors job2 tasklist) job1)
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

#_(defn critical_path)

(defn depends_on
  "takes a job and the list of tasks then returns a list of the jobs that cannot be started until this job has completed. This should return all future jobs, not just the ones immediately affected."
  [job tasklist]
  (map #(first %) (filter #(.contains (rest %) job) (map #(cons (first %) (rest (rest %))) tasklist))))

(println (get_all_preds 'siding tasks))
(println (get_max '(frame roof select_subs) tasks))
