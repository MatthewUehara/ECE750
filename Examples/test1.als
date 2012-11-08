/*
All clafers: 4 | Abstract: 0 | Concrete: 4 | References: 0
Constraints: 0
Goals: 0
Global scope: 1..1
All names unique: True
*/
open util/integer
pred show {}
run  show for 1 but 1 c1_car, 1 c2_engine

one sig c1_car
{ r_c2_engine : one c2_engine
, r_c3_radio : lone c3_radio }

one sig c2_engine
{}

lone sig c3_radio
{ r_c4_cdPlayer : lone c4_cdPlayer }
{ one r_c3_radio
  let children = (r_c4_cdPlayer) | some children }

lone sig c4_cdPlayer
{}
{ one r_c4_cdPlayer }

