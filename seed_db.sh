count=0
while true; do
	count=$((count+1))
	evar=$(/c/Program\ Files/PostgresSQL/pgsql/bin/psql.exe -c "INSERT INTO public.orders (id, customer_name, material_cost, tax_rate, total_tax, grand_total, date, labor_cost, area, cost_per_square_foot, labor_cost_per_square_foot, product_id, state_id ) VALUES (59, 'pat', 1, 2, 3, 4, now(), 6, 7, 8, 9, null, null )" -U myself "flooring_master")
	echo "Process"
	echo $evar
	echo ${#evar}
	if [ ${#evar} -ge 3 ]; then echo "Greater"; else echo "Less"; fi
	#if [ -z ${evar+x} ]; then echo "Still Trying."; sleep 1; else echo $evar; echo "Breaking."; break; fi
	if [ ${#evar} -le 3 ]; then echo "Still Trying.";echo $count; sleep 1; else echo $evar; echo "Breaking."; break; fi
done
echo "Total Count: '$count'"