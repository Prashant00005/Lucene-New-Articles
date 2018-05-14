#!/bin/bash

# Base Script File (run_all.sh)
# Created: mer. 28 févr. 2018 19:37:56 GMT
# Version: 1.0
#
# This Bash script was developped by Cory.
#
# (c) Corentin Chéron <chronc@tcd.ie>

set -e

out=../results.txt
trec_eval=../trec_eval.9.0/trec_eval
qrel=../qrels.assignment2.part1

$trec_eval -m map $qrel $out
$trec_eval  $qrel $out\
    | grep iprec | cut -d '_' -f 4 > rp_data_1stSubmission

gnuplot -c ./plot_script
