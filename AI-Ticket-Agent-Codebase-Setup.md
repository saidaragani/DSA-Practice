# AI Ticket-Solving Agent --- Codebase Understanding & Setup Request

## Goal

I am building an AI agent in GitHub Copilot Chat / an IDE agent to help
me solve development tickets in an existing legacy Java application.

The agent must NOT invent a new architecture or blindly apply modern
patterns.

It must first understand the existing codebase, identify the established
implementation pattern, find similar working examples, make the minimum
required changes, and validate the result.

The application uses a legacy Struts 2 architecture with Actions,
Commands, Gateways, IMessage-based messaging, business-service/PARIS
calls, faults, JSP/JavaScript, and ETM.

------------------------------------------------------------------------

# 1. IMPORTANT AGENT BEHAVIOR

The agent should follow this sequence for every ticket:

1.  Understand the ticket.
2.  Identify the affected LOB/feature/module.
3.  Trace the existing end-to-end flow.
4.  Find one or more similar already-working implementations.
5.  Identify the exact files that should be changed.
6.  Understand local coding conventions before editing.
7.  Propose the smallest safe change.
8.  Implement the change while preserving the existing architecture.
9.  Check related Struts configuration, Command, Gateway, IMessage,
    business-service/PARIS flow, JSP/JS, faults, and ETM where
    applicable.
10. Search for references/usages to make sure the change is complete.
11. Run the most relevant tests/build/validation available.
12. Report exactly what changed, why, and any remaining risks.

### Core rule

**Follow existing patterns first. Do not redesign the application unless
the ticket explicitly requires it.**

If there are multiple existing patterns, prefer the pattern used by the
closest/same feature.

------------------------------------------------------------------------

# 2. EXISTING HIGH-LEVEL ARCHITECTURE

The application appears to follow this general flow:

Browser / JSP / JavaScript ↓ \*.action request ↓ Servlet filters ↓
Struts interceptor stack ↓ Struts Action ↓ Gateway ↓ IMessage ↓ Business
Server / Command Gateway ↓ PARIS client / orchestration ↓ Business
processing ↓ IMessage response ↓ Fault handling ↓ Action result name ↓
Main.jsp ↓ Actual screen JSP

The exact implementation must be confirmed from the source code.

------------------------------------------------------------------------

# 3. STRUTS FLOW

The application uses Struts 2.

A typical screen has an Action class extending the application's base
screen Action.

There are commonly two paths:

### Render / GET path

prepareScreen() ↓ doPrepareScreen()

### Submit / POST path

processScreen() ↓ doProcessScreen()

The actual method names and inheritance should be confirmed from the
codebase.

Typical naming convention observed:

XxxScreen → prepare/render

XxxScreenSave → process/submit

Both may map to the same Action class with different methods.

The agent must inspect actual Struts configuration instead of assuming
this convention.

------------------------------------------------------------------------

# 4. STRUTS CONFIGURATION

The project has a base `struts.xml` and multiple included XML files
organized by LOB/area.

Examples seen include files such as:

-   common.xml
-   strutsRpq.xml
-   utilities.xml
-   async.xml
-   im.xml
-   wc.xml
-   farm.xml
-   gl.xml
-   rd.xml
-   auto.xml
-   umbrella.xml
-   property.xml
-   crime.xml
-   bop.xml

A typical mapping may look like:

``` xml
<action
    name="WCPolicyInfo"
    class="...actions.wc.WCPolicyInfoAction"
    method="prepareScreen"/>

<action
    name="WCPolicyInfoSave"
    class="...actions.wc.WCPolicyInfoAction"
    method="processScreen"/>
```

This is only an example. The agent must use the real project's
configuration.

------------------------------------------------------------------------

# 5. FILTERS AND INTERCEPTORS

The request may pass through servlet filters before reaching Struts.

Observed/requested flow includes things similar to:

DataDog/session-related filters ↓ policy response filter ↓ XSS filter ↓
StrutsPrepareAndExecuteFilter ↓ audit flush filter

The Struts interceptor stack may contain components such as:

logger MDC context ↓ eclps exception handling ↓ eclps session ↓ action
context ↓ eclps token / double-submit guard ↓ logger ↓ servlet config ↓
prepare ↓ chain ↓ debugging

The exact order and names must be read from the actual project
configuration.

Do not modify interceptor configuration unless the ticket requires it.

------------------------------------------------------------------------

# 6. ACTION LAYER

Find the Action class responsible for the ticket.

Determine:

-   package
-   superclass
-   implemented interfaces
-   injected dependencies
-   prepare method
-   process method
-   validation
-   Gateway usage
-   result names
-   session usage
-   fault handling
-   navigation behavior
-   ETM usage if present

Before changing an Action, search for similar Actions in the same LOB.

The agent should answer:

-   What does this Action own?
-   What should NOT be added to the Action?
-   Which logic belongs in Gateway?
-   Which logic belongs in Command?
-   Which logic belongs in Business Service/PARIS?

------------------------------------------------------------------------

# 7. GATEWAY LAYER

Gateways are used to communicate with the business/server side.

Typical pattern observed:

``` text
Gateway
   ↓
createMessage(name, LOB)
   ↓
IMessage
   ↓
populate message data
   ↓
callBusinessServer(IMessage)
   ↓
IMessage response
```

The actual base Gateway and helper methods must be inspected.

For each Gateway, understand:

-   superclass
-   message creation
-   message name/type
-   LOB
-   parameters added to IMessage
-   business server invocation
-   response handling
-   fault handling
-   exceptions
-   logging
-   ETM

The agent must reuse existing Gateway helpers instead of creating a
parallel communication mechanism.

------------------------------------------------------------------------

# 8. COMMAND LAYER

Commands represent business operations sent through the command
infrastructure.

Typical conceptual flow:

``` text
Action
   ↓
Gateway
   ↓
IMessage
   ↓
CommandGatewayController
   ↓
CommandMessageProcessor
   ↓
PARIS
```

Example classes may be named like:

``` text
TransactionCommand.java
SomeBusinessCommand.java
SomeOperationCommand.java
```

The agent must inspect a real existing Command before creating or
modifying one.

Determine:

-   command name
-   input data
-   IMessage structure
-   execution method
-   business service invocation
-   response structure
-   faults
-   transaction behavior
-   ETM
-   logging

------------------------------------------------------------------------

# 9. IMessage / MESSAGING

The project uses an `IMessage` abstraction.

The agent must understand:

-   how IMessage is created
-   how values are added/read
-   message names/types
-   LOB information
-   request vs response
-   nested data structures if present
-   fault information
-   validation information
-   session/context information

Do not replace IMessage with DTO/REST/etc. unless the existing project
pattern or ticket explicitly requires it.

Find real examples before making changes.

------------------------------------------------------------------------

# 10. BUSINESS SERVICE / PARIS

The project has business-server-side processing through a PARIS
client/orchestration layer.

Conceptual flow:

``` text
Gateway
   ↓
CommandGatewayController
   ↓
CommandMessageProcessor
   ↓
PARIS client/orchestration
   ↓
Business service
   ↓
Result / IMessage response
```

The agent must determine where the actual business logic for the ticket
lives.

Important:

Do not assume the business logic belongs in the web Action.

First search for:

-   existing business service
-   existing command
-   existing PARIS operation
-   existing message
-   similar ticket implementation
-   service methods used by neighboring features

Reuse existing business logic where possible.

------------------------------------------------------------------------

# 11. FAULT HANDLING

The project has its own fault/error model.

Observed conceptual handling:

``` text
IMessage response
      ↓
Gateway.handleFaults()
      ↓
CRITICAL / EXCEPTION
      → ECLPSException

VALIDATION
      → List<Fault>

INFO
      → session / informational messages
```

The exact implementation must be confirmed.

The agent must never introduce generic exception handling that bypasses
the application's existing fault mechanism.

When changing behavior, check:

-   validation faults
-   business faults
-   critical exceptions
-   user-facing messages
-   session messages
-   navigation/result behavior

------------------------------------------------------------------------

# 12. STRUTS RESULT / VIEW LAYER

The Action eventually returns a Struts result name such as:

``` text
input
success
error
navigate
```

The actual project may have more.

The result may resolve through:

``` text
Main.jsp
    ↓
Internal=...
    ↓
LOB
    ↓
screen JSP
```

Observed structure includes:

``` text
webapp/ecliq/templates/Main.jsp
webapp/ecliq/screens/<lob>/...
webapp/ecliq/js/
webapp/ecliq/css/
```

The agent must trace the actual result mapping before changing JSPs or
result names.

------------------------------------------------------------------------

# 13. JSP / JAVASCRIPT

The view layer includes JSP screens and JavaScript.

Observed areas include:

``` text
webapp/ecliq/templates/Main.jsp
webapp/ecliq/screens/
webapp/ecliq/js/
webapp/ecliq/css/
```

There may also be:

-   Struts tags
-   custom UI tags
-   feature toggles
-   async calls
-   REST/Spring endpoints
-   DOM manipulation
-   validation

Before changing a UI behavior, search:

1.  JSP
2.  JS
3.  Action
4.  Struts mapping
5.  related tags/helpers
6.  backend call

Do not change only the JSP if the behavior is controlled elsewhere.

------------------------------------------------------------------------

# 14. OTHER ENTRY POINTS

Not every request necessarily enters through `*.action`.

Observed entry points include:

``` text
/rest/*          → Spring MVC dispatcher / JSON APIs

/api/*           → another DispatcherServlet

/CommanderServlet → PARIS CommandServlet

/CredHandler
/DamMappingGroups
/serviceTest/*
/togglez/*
                  → utility servlets
```

The agent must determine which entry point the ticket actually uses.

------------------------------------------------------------------------

# 15. MODULE STRUCTURE

Observed modules include:

``` text
eclps-web
    Struts Actions
    interceptors
    JSP/JS/CSS
    session management
    gateways

eclps-common
    shared low-level code
    ActionContext
    entities/constants
    IMessage
    MessageHelper
    MessageDelegate
    MessageFault
    mock communications
    reference data

eclps-command-gateway
    CommandGatewayController
    CommandMessageProcessor
    CommandMessageRequest
    CommandMessageResponse
    session-policy/cache-related components

eclps-paris-client
eclps-paris-orchestration
    PARIS/business server-side execution

eclps-ear
    packaging/deployment
```

These names and boundaries must be verified against the actual source
tree.

------------------------------------------------------------------------

# 16. ETM

ETM is part of the existing application flow and must be understood from
real code.

For ETM, collect at least one complete real example.

The agent should determine:

-   where ETM is initialized
-   how ETM events/transactions are created
-   naming conventions
-   required fields
-   success/failure behavior
-   transaction boundaries
-   logging
-   whether ETM is Action-level, Gateway-level, Command-level, or
    business-service-level

Do not invent ETM usage.

If a ticket changes an operation that has ETM tracking, check whether
ETM also needs modification.

------------------------------------------------------------------------

# 17. REQUIRED REFERENCE FILES TO COLLECT

Before building the final agent rules, inspect these representative
files from the actual codebase.

## A. Action

One complete representative Action.java.

Prefer an Action that has both:

``` text
prepareScreen()
processScreen()
```

if possible.

## B. Command

One complete representative Command.java.

## C. Gateway

One complete representative Gateway.java.

## D. IMessage

A real example showing:

-   message creation
-   data population
-   response reading

## E. Fault handling

A real example showing validation/business/exception fault handling.

## F. Business Service

A real example showing how a Gateway/Command eventually invokes business
logic.

## G. PARIS

A real example of the PARIS/client/orchestration side.

## H. Struts configuration

The relevant LOB XML plus the base/global Struts configuration if
needed.

## I. JSP

The JSP belonging to the same feature as the Action.

## J. JavaScript

The JS belonging to the same feature if applicable.

## K. ETM

One real ETM implementation example.

## L. Completed ticket

If available, provide one previously completed ticket including:

-   ticket requirement
-   files changed
-   final implementation

This is extremely useful because it teaches the agent how the team
actually solves tickets.

------------------------------------------------------------------------

# 18. BEST WAY TO COLLECT THE REFERENCE CODE

Do NOT randomly collect hundreds of files.

Start with ONE complete feature/ticket.

For example:

``` text
WCPolicyInfo
    ↓
WCPolicyInfoAction.java
    ↓
WCPolicyInfo / related Command.java
    ↓
WCPolicyInfo / related Gateway.java
    ↓
IMessage
    ↓
Business Service
    ↓
PARIS
    ↓
Fault handling
    ↓
Struts XML
    ↓
WCPolicyInfo.jsp
    ↓
wcPolicyInfo.js
    ↓
ETM
```

Use this feature to learn the project's conventions.

Then add a second example if the first one does not cover an important
pattern.

------------------------------------------------------------------------

# 19. CODEBASE INVESTIGATION RULES FOR THE AGENT

Before editing code, the agent should:

### Step 1 --- Identify entry point

Find:

-   URL/action name
-   Struts mapping
-   Action class

### Step 2 --- Trace Action

Find:

-   prepare/process method
-   Gateway calls
-   validation
-   results

### Step 3 --- Trace Gateway

Find:

-   IMessage creation
-   message fields
-   business server call
-   fault handling

### Step 4 --- Trace Command

Find:

-   command mapping
-   execution
-   message processing

### Step 5 --- Trace business logic

Find:

-   PARIS
-   business service
-   DAO if applicable

### Step 6 --- Trace response

Find:

-   faults
-   validation
-   session messages
-   Action result
-   JSP

### Step 7 --- Trace UI

Find:

-   JSP
-   JS
-   Struts tags
-   REST calls if applicable

### Step 8 --- Trace ETM

Find:

-   transaction/event tracking
-   existing naming pattern
-   success/failure recording

------------------------------------------------------------------------

# 20. SIMILAR-CODE-FIRST RULE

Before creating a new class/method/configuration:

Search for an existing implementation.

Search by:

-   class naming pattern
-   method name
-   command name
-   gateway name
-   message name
-   JSP name
-   LOB
-   business operation
-   ETM event
-   fault handling
-   Struts result

Prefer copying the STRUCTURE/PATTERN of the closest existing
implementation rather than designing from scratch.

------------------------------------------------------------------------

# 21. MINIMUM CHANGE RULE

The agent should make the smallest change required by the ticket.

Avoid:

-   unnecessary refactoring
-   renaming unrelated classes
-   migrating frameworks
-   converting legacy code to modern patterns
-   changing architecture
-   introducing new libraries
-   changing shared infrastructure without necessity
-   formatting huge unrelated files
-   changing unrelated behavior

If a larger change appears necessary, explain why before doing it.

------------------------------------------------------------------------

# 22. IMPACT ANALYSIS

Before editing, determine:

``` text
Ticket
  ↓
Entry point
  ↓
Action
  ↓
Gateway
  ↓
Command
  ↓
IMessage
  ↓
Business Service / PARIS
  ↓
Faults
  ↓
Result
  ↓
JSP / JS
  ↓
ETM
```

Mark each layer as:

``` text
REQUIRED CHANGE
POSSIBLE CHANGE
NO CHANGE
UNKNOWN — INVESTIGATE
```

This prevents missing a dependency.

------------------------------------------------------------------------

# 23. VALIDATION CHECKLIST

After implementation, check:

### Compilation

-   Java compiles
-   imports are correct
-   method signatures match
-   no accidental API changes

### Struts

-   action mapping is correct
-   method name is correct
-   result mapping is correct
-   interceptor behavior is preserved

### Gateway

-   correct message is created
-   correct LOB/message name is used
-   correct business call is made

### Command

-   correct command is executed
-   request/response structures match

### IMessage

-   fields are correctly populated
-   response fields are correctly read

### Faults

-   existing fault mechanism is preserved
-   validation errors still work
-   exceptions are handled according to project conventions

### UI

-   JSP references are correct
-   JS references are correct
-   result names lead to the correct screen

### ETM

-   tracking remains correct
-   new operation is tracked if required

### Regression

-   search usages/references
-   run relevant tests
-   run build/module tests
-   inspect changed files

------------------------------------------------------------------------

# 24. AGENT RESPONSE FORMAT

For every ticket, the agent should communicate clearly.

Use this structure:

## Understanding

Explain the ticket in simple terms.

## Existing Flow

``` text
entry point
→ Action
→ Gateway
→ IMessage
→ Command
→ Business Service/PARIS
→ response
→ faults
→ result
→ JSP
```

## Similar Implementation Found

List the closest existing class/file and explain why it is relevant.

## Impacted Files

``` text
FILE 1 — reason
FILE 2 — reason
FILE 3 — reason
```

## Proposed Changes

Explain only the required changes.

## Implementation

Make the changes.

## Validation

Show:

-   searches performed
-   tests/build executed
-   important checks

## Final Summary

``` text
Changed:
- ...

Not changed:
- ...

Reason:
- ...

Potential risk:
- ...
```

------------------------------------------------------------------------

# 25. IMPORTANT: ASK FOR MISSING INFORMATION ONLY WHEN NECESSARY

The agent should investigate the repository first.

Do not immediately ask me:

"Which file should I change?"

Instead:

1.  Search the codebase.
2.  Find the likely implementation.
3.  Compare similar code.
4.  Determine the flow.
5.  Only ask me if the repository does not provide enough information or
    there are genuinely multiple ambiguous business interpretations.

------------------------------------------------------------------------

# 26. DO NOT ASSUME MODERN ARCHITECTURE

This is a legacy application.

Do NOT automatically convert:

``` text
Action → Gateway → IMessage → Command → PARIS
```

into:

``` text
Controller → Service → Repository → DTO
```

Do not introduce:

-   Spring Boot patterns
-   REST controllers
-   JPA
-   repositories
-   DTO layers
-   dependency injection changes
-   modern exception frameworks

unless the ticket explicitly requires them and the existing project
already supports the pattern.

------------------------------------------------------------------------

# 27. LANGUAGE / CHAT BEHAVIOR

When talking to me in Copilot Chat:

**Default language: Telugu written using English letters.**

Example:

> "Ee ticket lo first request ekkada enter avutundo chuddam. Tarvata
> existing similar implementation search chesi, exact ga ye files change
> cheyyalo identify chestha."

Another example:

> "Ikkada Gateway already IMessage create chesthundi kabatti kottha
> messaging logic create cheyyalsina avasaram ledu."

If I explicitly ask:

> "Explain in English"

then explain in English.

Code, class names, method names, commands, errors, and technical
identifiers should remain in their original form.

------------------------------------------------------------------------

# 28. FINAL OBJECTIVE

The final AI agent should behave like a developer who already
understands this project's conventions.

For a new ticket:

``` text
READ TICKET
   ↓
UNDERSTAND REQUIREMENT
   ↓
FIND ENTRY POINT
   ↓
TRACE EXISTING FLOW
   ↓
FIND SIMILAR IMPLEMENTATION
   ↓
UNDERSTAND LOCAL PATTERN
   ↓
IMPACT ANALYSIS
   ↓
MINIMUM SAFE CHANGE
   ↓
IMPLEMENT
   ↓
CHECK ALL REFERENCES
   ↓
TEST / BUILD
   ↓
VERIFY FAULTS + ETM + UI
   ↓
REPORT
```

The most important principle is:

**UNDERSTAND FIRST → COPY EXISTING PATTERN → CHANGE MINIMUM → VERIFY
EVERYTHING.**

Do not guess the architecture. Read the repository and follow what the
repository already does.
