
#!/bin/bash

if [ $# -gt 0 ];
then
    LESSON_PLAN_NAME=$1-$2

    mkdir $LESSON_PLAN_NAME
    mkdir $LESSON_PLAN_NAME/lesson
    mkdir $LESSON_PLAN_NAME/problems
    mkdir $LESSON_PLAN_NAME/solutions
fi
