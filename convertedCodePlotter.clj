(defn draw-line [from to color]
  (println (str "Рисуем линию из (" (:x from) ", " (:y from) ") в (" (:x to) ", " (:y to) ") используя " color " цвет.")))

(defn calc-new-position [distance angle current]
  (let [angle-in-rads (* angle (/ Math/PI 180.0))]
    {:x (Math/round (+ (:x current) (* distance (Math/cos angle-in-rads))))
     :y (Math/round (+ (:y current) (* distance (Math/sin angle-in-rads))))}))

(defn move [distance current-state]
  (let [new-position (calc-new-position distance (:angle current-state) (:position current-state))]
    (if (= (:carriage-state current-state) :DOWN)
      (draw-line (:position current-state) new-position (:color current-state))
      (println (str "Передвигаем на " distance " от точки (" (:x (:position current-state)) ", " (:y (:position current-state)) ")"))) 
    {:position new-position
     :angle (:angle current-state)
     :color (:color current-state)
     :carriage-state (:carriage-state current-state)}))

(defn turn [angle current-state]
  (println (str "Поворачиваем на " angle " градусов"))
  {:position (:position current-state)
   :angle (+ (:angle current-state) angle)
   :color (:color current-state)
   :carriage-state (:carriage-state current-state)})

(defn carriage-up [current-state]
  (println "Поднимаем каретку")
  {:position (:position current-state)
   :angle (:angle current-state)
   :color (:color current-state)
   :carriage-state :UP})

(defn carriage-down [current-state]
  (println "Опускаем каретку")
  {:position (:position current-state)
   :angle (:angle current-state)
   :color (:color current-state)
   :carriage-state :DOWN})

(defn set-color [color current-state]
  (println (str "Устанавливаем " color " цвет линии."))
  {:position (:position current-state)
   :angle (:angle current-state)
   :color color
   :carriage-state (:carriage-state current-state)})

(defn set-position [position current-state]
  (println (str "Устанавливаем позицию каретки в (" (:x position) ", " (:y position) ")."))
  {:position position
   :angle (:angle current-state)
   :color (:color current-state)
   :carriage-state (:carriage-state current-state)})

(defn draw-triangle [size current-state]
  (let [steps (range 3)
        updated-state (carriage-down current-state)]
    (let [final-state (reduce (fn [state _]
                                (-> state
                                    (move size)
                                    (turn 120.0)))
                              updated-state
                              steps)]
      (carriage-up final-state))))

(defn draw-square [size current-state]
  (let [steps (range 4)
        updated-state (carriage-down current-state)]
    (let [final-state (reduce (fn [state _]
                                (-> state
                                    (move size)
                                    (turn 90.0)))
                              updated-state
                              steps)]
      (carriage-up final-state))))

(defn plotter-process []
  (let [init-position {:x 0.0 :y 0.0}
        init-color "черный"
        init-angle 0.0
        init-carriage-state :UP

        plotter-state {:position init-position
                       :angle init-angle
                       :color init-color
                       :carriage-state init-carriage-state}]
    (let [plotter-state (draw-triangle 100.0 plotter-state)]
      (let [plotter-state (set-position {:x 10.0 :y 10.0} plotter-state)]
        (let [plotter-state (set-color "красный" plotter-state)]
          (draw-square 80.0 plotter-state))))))

(plotter-process)