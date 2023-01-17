import {XMLParser} from 'fast-xml-parser';

export default function parseWorkflowXMLToObject(xmlContent: string) {
  const tasks = new XMLParser({
    ignoreAttributes: false,
    attributeNamePrefix: '@_',
    allowBooleanAttributes: true,
    preserveOrder: true,
  })
    .parse(xmlContent)[0]
    ['bpmn2:definitions'][0]['bpmn2:process'].filter((t) => t['bpmn2:task']);
  return {
    nodes: tasks.map((t, i) => ({
      id: t[':@']['@_id'],
      position: {x: i * 200, y: 0},
      data: {label: t[':@']['@_name']},
      sourcePosition: 'right',
      targetPosition: 'left',
    })),
    edges: tasks.slice(0, -1).map((n, i) => ({
      id: `e${i}`,
      source: tasks[i][':@']['@_id'],
      target: tasks[i + 1][':@']['@_id'],
    })),
  };
}
