#include <stdio.h>
#include <sys/types.h>
#include <netdb.h>
#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>
#include <stdlib.h>
#include<string.h>

// Rôle du serveur : accepter la demande de connexion d'un client,
// recevoir une chaîne de caractères, afficher cette chaîne et
// renvoyer au client le nombre d'octets reçus par le serveur.

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
  server.sin_port = htons(atoi(argv[1]));
  
  if(bind(ds,(struct sockaddr*) &server, sizeof(server)) < 0){
    perror("Serveur : erreur bind");
    close(ds);
    exit(1);
  }

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
  struct in_addr addrClients[5];
  unsigned short portsClients[5];
  int cptClient = 0;

  while(cptClient < nbClients){

	  printf("Serveur : j'attends la demande d'un client (accept) \n");

	  
	  struct sockaddr_in adCv ; // obtenir adresse client accepté
	  socklen_t lgCv = sizeof (struct sockaddr_in);

	  int dsCv = accept(ds,(struct sockaddr *) &adCv, &lgCv);
	  if (dsCv < 0){
	    perror ( "Serveur, probleme accept :");
	    close(ds);
	    exit (1);
	  }
	  
	  /* affichage adresse socket client accepté :
	     adresse IP et numéro de port de structure adCv. 
	     Attention conversions format réseau -> format hôte.
	     fonction inet_ntoa(..) pour l'IP. */

	  char* ipserv = inet_ntoa(adCv.sin_addr);
	  int port = htons(server.sin_port);
	  printf("Serveur: le client %s:%d est connecté  \n", ipserv, port);

	  //on a un client on stocke donc son adresse dans notre tableau addrClients
	  //ATTENTION adresse sera dans addrClients[cptClient] au format réseau

	  addrClients[cptClient] = adCv.sin_addr;

	  

	  //Maintenant le client doit envoyer le numéro de port sur lequel il sera accéssible pour son voisin

	  /* réception message */
	 
	  unsigned short buffer[10*sizeof(unsigned short)];

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

	  /*buffer[strlen(buffer)+1]='\0';*/
	  /* Afficher nb octets EFFECTIVEMENT reçus
	                /!\
	    nb octets buffer réception socket != nb octets extraits */
	  
	  printf("Serveur : j'ai recu %d octets \n", rcv);
	  printf("Serveur : contenu du message : %i \n", buffer[0]);

	  //on stocke le num de port
	  portsClients[cptClient] = buffer[0];

	  //incrémentation nb clients enregistrés
	  cptClient++;

	  /* Reponse client = nb octets effectivement reçus */ 

	  // int snd = send(dsCv,&rcv,sizeof(rcv),0);
	  
	  // if (snd < 0){ 
	  //   perror ( "Serveur, probleme envoi :");
	  //   close(dsCv);
	  //   close(ds);
	  //   exit (1);
	  // }
	  // else if (snd == 0)
	  // {
	  //   printf("Serveur : socket fermée ?!?\n");
	  //   close(dsCv);
	  //   close(ds);
	  //   exit (1);
	  // }
	    
	  // /*fermeture socket client */ 

	  printf("Serveur : fin du dialogue avec le client\n");
	  close (dsCv);
	}
  
  /*fermeture socket demandes */
  close (ds);
  printf("Serveur : je termine\n");
}
