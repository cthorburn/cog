a = true;
b = false;
c = functionCall(a);

if ( a && b ) {
  log "1 :: a=" + a +", b=" + b;
}
else if ( a || b) {
  log "2 :: a=" + a +", b=" + b;
}
else {
  log "3 :: a=" + a +", b=" + b;
}

log "Done!";


while(c>0){
	
	if(c%2==0) {
		log "mod works!";
		exit_loop;
		log "mod loop after break!";
	}
	log "count!" + c;
}
log "exit!";