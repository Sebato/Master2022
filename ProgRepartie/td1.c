#include <stdio.h>
#include <sys/types.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdlib.h>
#include<string.h>
//#include "test/strtok.c"

// Rôle du serveur : accepter la demande de connexion d'un client,
// recevoir une chaîne de caractères, afficher cette chaîne et
// renvoyer au client le nombre d'octets reçus par le serveur.
void sep(char* debut,char* fin,char* tab, int size, char c){
 int i = 0;
 printf("%i\n",size);
 while(tab[i]!=c){
   debut[i] = tab[i];
   i++;
 }
 
 i++;
 int j = 0;
 while(i < size) {
   fin[j] = tab[i];
   printf("%c\n",tab[i] );
   j++;
   i++;
 }
}
int main(int argc, char *argv[])
{
  // paramètre = num port socket d'écoute
  
  if (argc<2){
    printf("utilisation: %s numero_port nombre_clients\n", argv[0]);
    exit(1);
  }

   //nombre de clients à relier 
  int nbClients = atoi(argv[2]);
  
  /* creation socket permet recevoir demandes connexion.*/
  int ds = socket(PF_INET, SOCK_STREAM, 0);

  if (ds == -1){
    perror("Serveur : probleme creation socket");
    exit(1);
  }
  
   printf("Serveur: creation de la socket : ok\n");
 
  
  /* nommage socket
     Elle aura une ou des IP de la machine sur laquelle 
     le programme sera exécuté et le numéro de port passé
     en paramètre
  */

  struct sockaddr_in server;
  server.sin_family = AF_INET;
  server.sin_addr.s_addr = INADDR_ANY;
  server.sin_port =  htons(atoi(argv[1]));
  
  if(bind(ds,(struct sockaddr*) &server, sizeof(server)) < 0){
    perror("Serveur : erreur bind");
    close(ds);
    exit(1);
  }

  printf("Serveur: adresse = %s:%d est connecté  \n", inet_ntoa(server.sin_addr), server.sin_port);

  printf("Serveur: nommage : ok\n");

  /* socket nommée -> ecoute
    dédie socket à réception demandes connexion
    & limiter file demandes connexions */
  
  int ecoute = listen(ds,nbClients);
  if (ecoute < 0){
    printf("Serveur : je suis sourd(e)\n");
    close (ds);
    exit (1);
  } 
 
  printf("Serveur: mise en écoute : ok\n");
 
  /* attendre et traiter demande connexion client. 
     serveur accepte demande = creation nouvelle socket
     connectée au client à utiliser pour
     communiquer avec lui.*/

  //tableau des sockets clients ici
  char addrClients[nbClients][20];
  char portsClients[10];
  int cptClient = 0;

  while(cptClient < nbClients){

	  printf("Serveur : j'attends la demande d'un client (accept) \n");

	  
	  struct sockaddr_in adC ; // obtenir adresse client accepté
	  socklen_t lgC = sizeof (struct sockaddr_in);

	  printf("Serveur : debug1\n");

	  int dsCv = accept(ds,(struct sockaddr *) &adC, &lgC);
	  if (dsCv < 0){
	    perror ( "Serveur, probleme accept :");
	    close(ds);
	    exit (1);
	  }
	  printf("Serveur : debug2\n");
	  
	  /* affichage adresse socket client accepté :
	     adresse IP et numéro de port de structure adC. 
	     Attention conversions format réseau -> format hôte.
	     fonction inet_ntoa(..) pour l'IP. */

	  char* ipserv = inet_ntoa(adC.sin_addr);
	  int port = htons(server.sin_port);
	  printf("Serveur: le client %s:%d est connecté  \n", ipserv, port);

	  
	  //portsClients[cptClient] = adC.sin_port;


	  //Maintenant le client doit envoyer le numéro de port sur lequel il sera accéssible pour son voisin

	  /* réception message */
	 
	  char buffer[100];

	  /* attendre message taille max = 10 shorts. */
	  //char stop=fgetc(stdin);

	  int rcv = recv(dsCv, buffer, sizeof(buffer),0) ;

	  if (dsCv < 0){ 
	    perror ( "Serveur, probleme reception :");
	    close(dsCv);
	    close(ds);
	    exit (1);
	  }
	  else if (dsCv == 0)
	  {
	    printf("Serveur : socket fermée ?!?\n");
	    close(dsCv);
	    close(ds);
	    exit (1);
	  }
	  
	  printf("Serveur : j'ai recu %d octets \n", rcv);
	  printf("Serveur : contenu du message : %s \n", buffer);


	  //on a un client on stocke donc son adresse dans notre tableau addrClients
	  //ATTENTION adresse sera dans addrClients[cptClient] au format réseau

	 sep(addrClients[cptClient],portsClients,buffer, rcv, ':');



	  printf("Serveur : addrClients[0] = %s  \n", addrClients[cptClient]);
	  printf("Serveur : portsClients[0] = %s \n", portsClients);

	  //incrémentation nb clients enregistrés
	  cptClient++;

	    
	  // /*fermeture socket client */ 

	  printf("Serveur : fin du dialogue avec le client\n");
	  close (dsCv);
	}
  
  /*fermeture socket demandes */
  close (ds);
  printf("Serveur : je termine\n");
}
