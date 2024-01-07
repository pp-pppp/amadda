export interface CategoryCreateResponse {}
export interface CategoryUpdateResponse {}
export interface CategoryReadResponse {
  categorySeq: number;
  categoryName: string;
  categoryColor: 'SALMON' | 'YELLOW' | 'CYAN' | 'ORANGE' | 'HOTPINK' | 'GREEN' | 'GRAY';
}
