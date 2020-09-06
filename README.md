# Jira-Assistant-with-Slack-API
Planned as a Jira assistant does some checks on every Sprint planning and notifies team members via Slack.

## Sample Usage

### Story Point Comparison

Given an Excel sheet that is used by Scrum Master as template during the planning of the next Sprint...

| Epic        | Priority | PBI    | PBI Definition  | Sprint   | Assignee | Note | Story Point |
|-------------|----------|--------|-----------------|----------|----------|------|-------------|
| Deployment  | 1        | IMA-17 | New Deployment  | Sprint53 | Sam      | -    | 3           |
| Development | 2        | IMA-18 | New Development | Sprint53 | Herald   | -    | 5           |
| Analysis    | 3        | IMA-19 | New Analysis    | Sprint53 | Anna     | -    | 8           |
| Design      | 4        | IMA-20 | New Design      | Sprint53 | Benedict | -    | 13          |

...planned Sprint can be checked by using ```compareStoryPoints()``` method on `JiraApi.java` whether if it's planned on Jira properly according to the Excel planning sheet.

![image](https://user-images.githubusercontent.com/36932448/92329102-ef976780-f06d-11ea-9518-a9d37e39591c.png)

```
Edit Story Points of 1 PBI(s) on Jira according to the Sprint Planning Excel sheet? (y/n)
```
Upon finding a mismatch between the Excel sheet and the content on Jira, it prompts the user to agree editing Jira content.
```
Story Point of IMA-20 has been changed to 13
```

## Setup

- `resources/credentials.properties` must be filled with necessary information such as `email`, `api_token`, and `jira_link`.
- The Excel sheet used for planning must be located in `src/main/resources/` folder and the path must be specified in `readExcel()` method on `ReadExcel.java`.
- If the target field on Jira to be changed is a custom field such as "Story Points" its name must be found on the response body using below code.
```java
response.getBody()
```
- Run the main method of `JiraApi.java`

## Next Step

- Integrating Slack Incoming WebHooks/Chatbot to notify the team members about the changes automatically.
