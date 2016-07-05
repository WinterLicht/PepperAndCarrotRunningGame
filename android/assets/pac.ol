{
	nodes:[
		{
			class: "story", id: 1, x: 0, y: 1, name: "Story 1",
			storyboard: {
				panels: [
					{atlasName: "storyAtlas", regionName: "panel1", duration: 1.0},
					{atlasName: "storyAtlas", regionName: "panel2", duration: 1.0},
					{atlasName: "storyAtlas", regionName: "panel3", duration: 1.0}
				]
			}
		},
		
		{
			class: "level", id: 2, x: 1, y: 1, name: "Level 1", condition: {conditionalCompletedNodeIds: [1]},
			levelLayout: {
				entrancePart: {
					shuffle: false,
					loopCount: 1,
					tmxMaps: ["startlevel.tmx", "level1.tmx", "level2.tmx"]
				},
				loopPart: {
					shuffle: true,
					loopCount: 3,
					tmxMaps: ["level1.tmx", "level2.tmx"]
				},
				exitPart: {
					tmxMaps: ["level2.tmx"]
				}
			}
		},
		
		{class: "level", id: 3, x: 2, y: 1, name: "Level 2", condition: {conditionalCompletedNodeIds: [2]}},
		{class: "story", id: 4, x: 3, y: 1, name: "Story 2", condition: {conditionalCompletedNodeIds: [3]}},
		{class: "level", id: 5, x: 3, y: 2, name: "Level 3", condition: {conditionalCompletedNodeIds: [4]}},
		{class: "level", id: 6, x: 4, y: 2, name: "Level 4", condition: {conditionalCompletedNodeIds: [5]}},
		{class: "level", id: 7, x: 5, y: 2, name: "Level 5", condition: {conditionalCompletedNodeIds: [6]}},
		{class: "story", id: 8, x: 5, y: 1, name: "Story 3", condition: {conditionalCompletedNodeIds: [7]}},
		{class: "level", id: 9, x: 3, y: 0, name: "Bonus Level 1", condition: {conditionalCompletedNodeIds: [3], additionalConditions: ["all-potions-level-2"]}},
		{class: "level", id: 10, x: 4, y: 0, name: "Bonus Level 2", condition: {conditionalCompletedNodeIds: [9]}},
		{class: "story", id: 11, x: 4, y: 1, name: "Bonus Story 1", condition: {conditionalCompletedNodeIds: [10]}},
		{class: "empty", id: 12, x: 2, y: 0, name: "Empty", condition: {conditionalCompletedNodeIds: [3], additionalConditions: ["all-potions-level-2"]}}
	],
	
	edges: [
		{sourceId: 1, destinationId: 2, condition: {conditionalCompletedNodeIds: [1]}},
		{sourceId: 2, destinationId: 3, condition: {conditionalCompletedNodeIds: [2]}},
		{sourceId: 3, destinationId: 4, condition: {conditionalCompletedNodeIds: [3]}},
		{sourceId: 4, destinationId: 5, condition: {conditionalCompletedNodeIds: [4]}},
		{sourceId: 5, destinationId: 6, condition: {conditionalCompletedNodeIds: [5]}},
		{sourceId: 6, destinationId: 7, condition: {conditionalCompletedNodeIds: [6]}},
		{sourceId: 7, destinationId: 8, condition: {conditionalCompletedNodeIds: [7]}},
		{sourceId: 3, destinationId: 12, condition: {conditionalCompletedNodeIds: [3], additionalConditions: ["all-potions-level-2"]}},
		{sourceId: 12, destinationId: 9, condition: {conditionalCompletedNodeIds: [3], additionalConditions: ["all-potions-level-2"]}},
		{sourceId: 9, destinationId: 10, condition: {conditionalCompletedNodeIds: [9]}},
		{sourceId: 10, destinationId: 11, condition: {conditionalCompletedNodeIds: [10]}},
		{sourceId: 11, destinationId: 8, condition: {conditionalCompletedNodeIds: [11]}},
	]
}