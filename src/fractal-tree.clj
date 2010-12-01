(import
  '(javax.swing JFrame JPanel)
  '(java.awt Color Graphics Graphics2D))

(defn draw-tree [g2d angle x y length branch-angle depth]
  (if (> depth 0)
    (let [new-x (->> (Math/toRadians angle) Math/sin (* length) (- x))
          new-y (->> (Math/toRadians angle) Math/cos (* length) (- y))
          new-length (* length (+ 0.75 (rand 0.1)))
          new-angle (fn [op] (op angle (* branch-angle (+ 0.75 (rand)))))]
      (.drawLine g2d x y new-x new-y)
      (draw-tree g2d (new-angle +) new-x new-y new-length branch-angle (- depth 1))
      (draw-tree g2d (new-angle -) new-x new-y new-length branch-angle (- depth 1)))))

(defn render [g w h]
  (doto g
    (.setColor Color/BLACK)
    (.fillRect 0 0 w h)
    (.setColor Color/GREEN))
  (let [init-length (/ (min w h) 5),
        branch-angle (* 10 (/ w h)),
        max-depth 12]
    (#'draw-tree g 0.0 (/ w 2) h init-length branch-angle max-depth)))

(defn create-panel []
  "Create a panel with a customised render"
  (proxy [JPanel] []
    (paintComponent [g]
      (proxy-super paintComponent g)
      (time (render g (.getWidth this) (.getHeight this))))))

(defn run []
  (let [frame (JFrame. "Clojure Fractal Tree")
        panel (create-panel)]
    (doto frame
      (.add panel)
      (.setSize 640 400)
      (.setVisible true))))

(run)