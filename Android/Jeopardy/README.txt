#Jeopardy
#Assignment 1
#Dylan Kern

1. Three more classes: MultipleChoiceQuestion, TrueFalseQuestion, and FillInTheBlankQuestion were made to add the new question types. Each one extends Question to allow them to be put into a Question array.

2. Since all the questions were together in an array, I did not want to pass all the questions between activities. This led to only having one activity that turns on/off layouts for the different questions. The main reason I chose this approach because I didn't like the transition between QuizActivity and CheatActivity where it looks like it opens a new window.

3. No Android specific issues. Mostly just syntax with Java such as casting Question to the more specific Question types, and checking if a Question is an instance of another type.