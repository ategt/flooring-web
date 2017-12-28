echo "$(pwd)"
echo "\nCompressing reports."
tar -cvf reports.tar ./target/surefire-reports/
echo "\nUploading reports."
curl -F item[picture]=@./reports.tar https://stormy-plains-11755.herokuapp.com/items
