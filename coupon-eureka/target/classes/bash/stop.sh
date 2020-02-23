#!/usr/bin/env bash
if [ -f "progressId.pid" ] ; then
    kill -9 `cat progressId.pid`
    rm progressId.pid
fi
isExist=`ps -ef | grep MonitorAndImprove | grep -v grep | awk '{print $2}'`
if [ ! -z "$isExist" ]; then
    kill -9 $isExist

fi