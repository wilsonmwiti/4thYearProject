
/*
Non-learning ghost model against the AggressiveGhosts controller.
*/

nodeExpansionThreshold = 30;
maximumSimulationLength = 100000;
pacManModel = new RandomNonRevPacMan();
ghostModel = new Legacy();
selectionPolicy = new LevineUcbSelectionPolicy(4000);
opponent = new AggressiveGhosts();

