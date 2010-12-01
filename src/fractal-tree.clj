(import
  '(javax.swing JFrame JPanel)
  '(java.awt Color Graphics Graphics2D))

(defn- draw-tree [g angle x y length branch-angle depth]
  (if (> depth 0)
    (let [new-x (->> (Math/toRadians angle) Math/sin (* length) (- x))
          new-y (->> (Math/toRadians angle) Math/cos (* length) (- y))
          new-rand-length #(->> (rand 0.1) (+ 0.75 ) (* length))
          new-rand-angle #(->> (rand) (+ 0.75) (* branch-angle) (% angle))]
      (.drawLine g x y new-x new-y)
      (draw-tree g (new-rand-angle +) new-x new-y (new-rand-length) branch-angle (- depth 1))
      (draw-tree g (new-rand-angle -) new-x new-y (new-rand-length) branch-angle (- depth 1)))))

(defn- render [g width height]
  (doto g
    (.setColor Color/BLACK)
    (.fillRect 0 0 width height)
    (.setColor Color/GREEN))
  (let [init-length (/ (min width height) 5),
        branch-angle (* 10 (/ width height)),
        max-depth 12]
    (draw-tree g 0.0 (/ width 2) height init-length branch-angle max-depth)))

(defn- create-panel []
  "Create a panel with a customised render"
  (proxy [JPanel] []
    (paintComponent [g]
      (proxy-super paintComponent g)
      (time (render g (.getWidth this) (.getHeight this))))))

(defn run-fractal-tree []
  (let [frame (JFrame. "Clojure Fractal Tree")
        panel (create-panel)]
    (doto frame
      (.add panel)
      (.setSize 640 400)
      (.setVisible true))))

(run-fractal-tree)