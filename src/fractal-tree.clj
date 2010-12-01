(import
  '(javax.swing JFrame JPanel)
  '(java.awt Color Graphics Graphics2D))

(defn- draw-tree [graphics angle x y length branch-angle depth]
  (if (> depth 0)
    (let [new-x (->> (Math/toRadians angle) Math/sin (* length) (- x))
          new-y (->> (Math/toRadians angle) Math/cos (* length) (- y))
          new-rand-length #(->> (rand 0.1) (+ 0.75 ) (* length))
          new-rand-angle #(->> (rand) (+ 0.75) (* branch-angle) (% angle))]
      (.drawLine graphics x y new-x new-y)
      (draw-tree graphics (new-rand-angle +) new-x new-y (new-rand-length) branch-angle (- depth 1))
      (draw-tree graphics (new-rand-angle -) new-x new-y (new-rand-length) branch-angle (- depth 1)))))

(defn- render [graphics width height]
  (doto graphics
    (.setColor Color/BLACK)
    (.fillRect 0 0 width height)
    (.setColor Color/GREEN))
  (let [init-length (/ (min width height) 5),
        branch-angle (* 10 (/ width height)),
        max-depth 12]
    (draw-tree graphics 0.0 (/ width 2) height init-length branch-angle max-depth)))

(defn- create-panel []
  "Create a panel with a customised render"
  (proxy [JPanel] []
    (paintComponent [graphics]
      (proxy-super paintComponent graphics)
      (time (render graphics (.getWidth this) (.getHeight this))))))

(defn run-fractal-tree []
  (let [frame (JFrame. "Clojure Fractal Tree")
        panel (create-panel)]
    (doto frame
      (.add panel)
      (.setSize 640 400)
      (.setVisible true))))

(run-fractal-tree)