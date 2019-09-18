(ns components.playground.todo.todo
  (:require [reagent.core :as r]))

; Todos is just a list of strings
(defonce todos (r/atom (vector)))
(defonce show-code (r/atom false))

(defn add-todo [todo-text]
  (js/console.log "Adding Todo Item with text:" todo-text)
  (swap! todos conj todo-text))

(defn delete-todo [todo-text]
  (js/console.log "Deleting todo:" todo-text)
  ;swap! takes a function that recieves the current value, which you can use for updating.
  (swap! todos (fn [c] (remove #{todo-text} c))))


(defn todo-elem [text]
  (fn []
    [:li.list-item
     [:span text]
     [:div.button-spacer]
     [:input {:type "button" :value "Delete" :on-click #(delete-todo text)}]]))

(defn todo-form []
  (let [val        (r/atom "")
        reset-val #(reset! val "")]
    (fn []
      [:<>
       [:input
        {:type "text"
         :value @val
         :on-change #(reset! val (-> % .-target .-value))
         :on-key-down #(case (.-key %)
                         "Enter" (do (add-todo @val)(reset-val))
                         nil)
         :placeholder "Enter a todo..."}]

       [:div.button-spacer]

       [:input.button-primary
        {:type "button"
         :value "Add"
         :on-click (fn [] (add-todo @val) (reset-val))}]

       [:div.button-spacer]

       [:input.button
        {:type "button"
         :value "Clear Todos"
         :on-click #(reset! todos [])}]
       [:ul#todo-list
        (for [todo @todos]
          ^{:key todo} [todo-elem todo])]])))


(defn todo-app []
  [:div.example-container
   [:h2 "Todo"]
   
   [todo-form]
   [:div#users]

   [:input.button
    {:type "button"
     :value (str (if @show-code "Hide" "Show") " Code")
     :on-click #(reset! show-code (not @show-code))}]
   (when @show-code
     [:div "coming soon. :P"])])