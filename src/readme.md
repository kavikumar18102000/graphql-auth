**UserInfoConfig class**: Config class which provides core user info, which is later encapsulated into Authentication Objects.

**UserInfoManagerConfig class**: that implements the UserDetailsService interface, used to retrieve user-related data, 
                                 using loadUserByUsername(), and returns UserDetails.

* Authentication is the process of checking if a user is who they claim to be, while principal is the user who has been verified.

* httpBasic(Customizer.withDefaults()): “Handle login automatically using HTTP Basic”, no custom loginHandling
  → So Spring internally adds a filter: BasicAuthenticationFilter
  → httpBasic is:
      * Stateless
      * Header-based
      * Fully handled by Spring Security filters
  → Pros:
  ✔ Simple
  ✔ No login API

  → Cons:
  ❌ Sends credentials every request
  ❌ Not used in production APIs


## Encryption key for JWT
Generating Asymmetric Keys with OpenSSL : You have the option to create asymmetric keys (public and private keys) using OpenSSL or utilize the provided files in the repository located at resources/certs.

Using OpenSSL (Optional) If you choose to generate your own keys, follow these steps:

1. Create a certs folder in the resources directory and navigate to it:

   → cd src/main/resources/certs

2. Generate a KeyPair : This line generates an RSA private key with a length of 2048 bits using OpenSSL (openssl genrsa). 
   It then specifies the output file (-out keypair.pem) where the generated private key will be saved. 
   The significance lies in creating a private key that can be used for encryption, decryption, and digital signatures in 
   asymmetric cryptography.

   → openssl genrsa -out keypair.pem 2048   

3. Generate a Public Key from the Private Key: This command extracts the public key from the previously generated 
   private key (openssl rsa). It reads the private key from the file specified by -in keypair.pem and outputs the 
   corresponding public key (-pubout) to a file named publicKey.pem. The significance is in obtaining the public key 
   from the private key, which can be shared openly for encryption and verification purposes while keeping the private key secure.

   → openssl rsa -in keypair.pem -pubout -out publicKey.pem 

4. Format the Private Key (keypair.pem) in Supported Format (PKCS8 format): This line converts the private key generated 
   in the first step (keypair.pem) into PKCS#8 format, a widely used standard for private key encoding (openssl pkcs8). 
   It specifies that the input key format is PEM (-inform PEM), the output key format is also PEM (-outform PEM), 
   and there is no encryption applied (-nocrypt). The resulting private key is saved in a file named private.pem. 
   The significance is in converting the private key into a standard format that is interoperable across different 
   cryptographic systems and applications.

   → openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out privateKey.pem

→ If you want to apply encryption to the private key while exporting it using OpenSSL, you can simply omit the -nocrypt 
  option in the openssl pkcs8 command. By doing so, OpenSSL will prompt you to enter a passphrase that will be used to 
  encrypt the private key Note: encrypting the private key adds an extra layer of security, but it also means that you'll 
  need to provide the passphrase whenever you want to use the private key for cryptographic operations.

**STATELESS** : A stateless architecture is one in which the server does not store any session data for a client. 
Instead, each request from the client contains all the information necessary to complete the request.

* Let's add token encoder and decoder **decoder **: The JwtEncoder takes a JwtClaimsSet object as an argument and returns
a JWT as a string. The JwtEncoder bean is created using the NimbusJwtEncoder class, which is an implementation of the 
JwtEncoder interface. The NimbusJwtEncoder class uses a JWKSource object to obtain the key used to sign the JWT. 
In this case, the key is obtained from an RSAKey object that is built using the rsaPublicKey() and rsaPrivateKey() 
methods of a rsaKeyRecord object.

* If the JWT token generated and then the user is removed from DB, even the use can access with the same token until 
token is expired.(One flaw... of JWT token, so the keep the token expiry time less along with JWT filter)

**_NOTE_**: JWT is not stored on server — it is stored on client and sent with every request
OAuth2 = Token-based authorization system
JWT = Format of token
Spring Resource Server = Validator of token

This app is NOT “using OAuth2 fully”
This app is using OAuth2-style JWT validation

* Why RSA is considered “enterprise-grade”
With RSA (typically RS256):
Private key → signs the token (only your auth service has it)
Public key → verifies the token (can be shared with any service)
👉 This enables decoupled verification—multiple services can validate tokens without sharing secrets.

When RSA is the right choice

Use RSA if you have:
Multiple services validating tokens
API gateway + microservices architecture
Need to expose public key (JWK endpoint)
Plan to scale authentication separately

👉 This is how systems like:
Google Auth
Auth0
Keycloak
work internally

## JWT Filter
Let's create the Filter
* OncePerRequestFilter: The filter is implemented as a subclass of OncePerRequestFilter, which ensures that the filter 
    is only applied once per request.
* OncePerRequestFilter = “Run my filter logic only once per request, no matter what happens internally”
* The filter uses the rsaKeyRecord object to obtain the RSA public and private keys used to sign and verify the JWTs.
* JWT
  Valid: The filter creates an Authentication object and sets it in the SecurityContextHolder. The Authentication object contains the user details and authorities extracted from the JWT.
  In-valid: If the JWT is not valid, the filter throws a ResponseStatusException with an HTTP 406 Not Acceptable status code

