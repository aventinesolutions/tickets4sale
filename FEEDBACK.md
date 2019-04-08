# Feedback 1.April 2019
from Developers at Vakantie Discounter

## Positive
* good formatting, code is really easy to read
* Good Readme, everything works as described
* nice test coverage

## Can be improved
* The most important: Re-frame is used, but db usage is totally wrong, it’s just a global atom. I understand, that it’s not his primary language, but it’s used differently even in the readme of the re-frame library.
* Great that he uses re-frame, but he missed the basics which are clearly explained on the very first page and the examples that come with re-frame.
* Would have been nice that if he picks re-frame, he also shows that he understands how to test it (for example with re-frame-test).
* Uberjar doesn’t pack javascript inside. Simple lein run doesn’t run cljs build with a clj.
* Not so good validation (if you have an incorrect month, it moves the cursor in the end of the string, which is annoying).
* Component views are using local state (no benefits of re-frame subs are used).
* He either doesn't like or know about parameter destructuring. Would make his code more concise.
* Not a big issue, but all functions are just global.
