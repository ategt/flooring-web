count=0
while true; do
	count=$((count+1))
	evar=$(psql -c "INSERT INTO public.orders (id, customer_name, material_cost, tax_rate, total_tax, grand_total, date, labor_cost, area, cost_per_square_foot, labor_cost_per_square_foot, product_id, state_id ) VALUES (59, 'pat', 1, 2, 3, 4, now(), 6, 7, 8, 9, null, null )" -U myself "flooring_master")
	if [ ${#evar} -le 3 ]; then echo "Still Trying. - '$count'"; sleep 1; else echo $evar; echo "Breaking."; break; fi
done
echo "Total Count: '$count'"